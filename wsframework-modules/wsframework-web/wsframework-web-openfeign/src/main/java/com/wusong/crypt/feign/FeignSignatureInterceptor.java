package com.wusong.crypt.feign;

import com.wusong.crypt.common.AuthConstants;
import com.wusong.crypt.common.AESEncryptor;
import com.wusong.crypt.common.Signature;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author p14
 */
@Slf4j
public class FeignSignatureInterceptor implements RequestInterceptor {
    private String ak;
    private String sk;
    private AESEncryptor aesEncryptor;


    public FeignSignatureInterceptor(String ak, String sk, AESEncryptor aesEncryptor) {
        this.ak = ak;
        this.sk = sk;
        this.aesEncryptor = aesEncryptor;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String requestTime= OffsetDateTime.now(ZoneOffset.UTC).format(AuthConstants.TIME_FORMATTER);
        requestTemplate.header(AuthConstants.HEADER_AK,ak);
        requestTemplate.header(AuthConstants.HEADER_TIME,requestTime );
        String signData;
        String body=null;
        if(requestTemplate.body()!=null){
            body=new String(requestTemplate.body(), StandardCharsets.UTF_8);
            if(aesEncryptor!=null){
                body=aesEncryptor.encryptGCM(sk,body);
            }
        }
        if(body!=null){
            requestTemplate.header(AuthConstants.HEADER_SIGN_BODY,"true");
            signData= String.format("%s%s%s%s%s%s",requestTime,
                    requestTemplate.method(), requestTemplate.path(),requestTemplate.queryLine(), requestTime,body);
        }else {
            signData= String.format("%s%s%s%s%s",requestTime,
                    requestTemplate.method(), requestTemplate.path(),requestTemplate.queryLine(), requestTime);
        }
        requestTemplate.header(AuthConstants.HEADER_SIGN, Signature.hmacSHA1(sk,signData));
        if(body!=null){
            requestTemplate.body(body);
        }
    }
}
