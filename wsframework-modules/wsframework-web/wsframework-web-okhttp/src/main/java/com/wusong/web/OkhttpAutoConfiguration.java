package com.wusong.web;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.annotation.PreDestroy;
import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author p14
 */
@ConditionalOnClass(OkHttpClient.class)
@ConditionalOnProperty(name = "wsframework.okhttp.enable",havingValue = "true",matchIfMissing = true)
@Import(HttpProperties.class)
@Slf4j
public class OkhttpAutoConfiguration {

    @Bean
    @Primary
    public synchronized OkHttpClient wsOkHttpClient(HttpProperties httpProperties){
        Dispatcher dispatcher=new Dispatcher();
        dispatcher.setMaxRequests(httpProperties.getMaxRequests());
        dispatcher.setMaxRequestsPerHost(httpProperties.getMaxRequestsPerHost());
        ConnectionPool connectionPool=new ConnectionPool(httpProperties.getMaxIdle(), httpProperties.getKeepAliveSeconds(),TimeUnit.SECONDS);
        if(client!=null){
            return client;
        }
        SSLContext sslContext=null;
        SSLSocketFactory sslSocketFactory=null;
        X509TrustManager trustManager=null;
        try {
            trustManager =new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            sslSocketFactory = sslContext.getSocketFactory();
        }catch (Throwable t){
            log.error(t.getLocalizedMessage(),t);
        }

        client= new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .sslSocketFactory(sslSocketFactory,trustManager)
                .readTimeout(httpProperties.getReadTimeoutMills(),TimeUnit.MILLISECONDS)
                .writeTimeout(httpProperties.getWriteTimeoutMills(),TimeUnit.MILLISECONDS)
                .connectTimeout(httpProperties.getConnectTimeoutMills(),TimeUnit.MILLISECONDS)
                .connectionPool(connectionPool)
                .build();
        return client;
    }

    private OkHttpClient client;

    @PreDestroy
    public void destroy(){
        Cache cache = this.client.cache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException e) {
                log.error(e.getLocalizedMessage(),e);
            }
        }
        this.client.dispatcher().executorService().shutdown();
        this.client.connectionPool().evictAll();
    }

}
