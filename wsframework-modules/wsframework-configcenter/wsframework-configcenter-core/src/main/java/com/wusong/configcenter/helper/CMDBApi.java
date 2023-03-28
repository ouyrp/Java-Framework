package com.wusong.configcenter.helper;

import com.google.gson.Gson;
import com.wusong.crypt.common.AESEncryptor;
import com.wusong.crypt.common.AuthConstants;
import com.wusong.crypt.common.Signature;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * CMDB接口有验签和加密
 * @author p14
 */
@Slf4j
public class CMDBApi {
    private final static CloseableHttpClient httpClient;
    private static CMDBApi instance;

    public static CMDBApi getInstance() {
        return instance;
    }

    public static void setInstance(CMDBApi instance) {
        CMDBApi.instance = instance;
    }

    static {

        SSLContext sslIgnoreContext=null;
        try {
            sslIgnoreContext=new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (Throwable e) {
            log.error("尝试禁用es ssl证书验证 失败",e);
            throw new RuntimeException(e);
        }
        httpClient =HttpClients.custom().setSSLContext(sslIgnoreContext).setDefaultRequestConfig(
                RequestConfig.custom().setSocketTimeout(3000).build()).build();
    }
    private String salt;
    private String ak;
    private String sk;
    private String cmdbUrl;

    public CMDBApi(String salt, String ak, String sk, String cmdbUrl) {
        this.salt = salt;
        this.ak = ak;
        this.sk = sk;
        this.cmdbUrl = cmdbUrl;
        if(!StringUtils.hasText(sk)){
            log.error("wsframework.cmdb.service.sk 或 CMDB_SK 未设置，无法获取cmdb信息");
            throw new RuntimeException("CMDB_SK 未设置，无法获取cmdb信息");
        }
        if(!StringUtils.hasText(cmdbUrl)){
            log.error("cmdbUrl 未设置，无法获取cmdb信息");
            throw new RuntimeException("CMDB_URL 未设置，无法获取cmdb信息");
        }
    }

    public String getIp(){
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "error-hostname";
        }
    }

    public String getConfigServer(String application, String env){
        String path=cmdbUrl+"/api/public/configServer?application=" +application+"&env="+env+"&ip="+getIp();
        try {
            CloseableHttpResponse response = httpClient.execute(new HttpGet(path));
            String urls= EntityUtils.toString(response.getEntity());
            if(!StringUtils.hasText(urls)){
                throw new RuntimeException("cmdb 返回的 configserver 地址为空，无法启动系统 "+urls);
            }
            // TODO 取第一个地址
            return urls.split(",")[0];
        } catch (IOException e) {
            throw new RuntimeException("获取 configserver 地址失败，请检查以下接口是否正常 "+path,e);
        }
    }

    public Map<String,String> getAllConfig(String application, String env){
        try {
            String path="/api/wsframework/allConfig?prefer=java&application=" +application+"&env="+env;
            HttpGet getRequest=new HttpGet(cmdbUrl+path);
            log.info("Fetch from cmdb {}",getRequest.getURI());
            String requestTime= OffsetDateTime.now(ZoneOffset.UTC).format(AuthConstants.TIME_FORMATTER);
            getRequest.setHeader(AuthConstants.HEADER_AK,ak);
            getRequest.setHeader(AuthConstants.HEADER_TIME,requestTime );
            String signData= String.format("%s%s%s%s",requestTime,
                    getRequest.getMethod(), path, requestTime);
            getRequest.setHeader(AuthConstants.HEADER_SIGN, Signature.hmacSHA1(sk,signData));

            CloseableHttpResponse response = httpClient.execute(getRequest);
            String body=EntityUtils.toString(response.getEntity());
            if(response.getFirstHeader(AuthConstants.HEADER_SIGN)!=null){
                AESEncryptor aesEncryptor=new AESEncryptor(salt);
                body=aesEncryptor.decryptGCM(sk,body);
            }
            Gson gson=new Gson();
            HashMap result = gson.fromJson(body, HashMap.class);
            if(!"200".equals(result.get("code"))){
                log.error("获取cmdb配置出错 result {}",result);
                throw new RuntimeException("获取cmdb配置出错 "+result);
            }
            return (Map<String, String>) result.get("data");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
