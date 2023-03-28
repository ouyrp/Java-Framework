package com.wusong.crypt.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wusong.crypt.common.AESEncryptor;
import com.wusong.crypt.common.AuthConstants;
import com.wusong.crypt.common.SecretKeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author p14
 */
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AESEncryptor aesEncryptor;
    @Autowired
    private SecretKeyStore secretKeyStore;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        List<String> ak = request.getHeaders().get(AuthConstants.HEADER_AK);
        if(ak!=null&&ak.size()==1){
            String sk=secretKeyStore.getSecretKey(ak.get(0));
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                response.getHeaders().set(AuthConstants.HEADER_SIGN,"any");
                OutputStream responseBody = response.getBody();
                responseBody.write(aesEncryptor.encryptGCM(sk,objectMapper.writeValueAsString(body)).getBytes(StandardCharsets.UTF_8));
                responseBody.flush();
                responseBody.close();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            return body;
        }
    }
}
