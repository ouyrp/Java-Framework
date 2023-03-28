package com.wusong.crypt.web;

import com.wusong.crypt.common.AESEncryptor;
import com.wusong.crypt.common.AuthConstants;
import com.wusong.crypt.common.SecretKeyStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author p14
 */
@Slf4j
public class DecryptRequestBodyAdvice extends RequestBodyAdviceAdapter {
    @Autowired
    private AESEncryptor aesEncryptor;
    @Autowired
    private SecretKeyStore secretKeyStore;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        List<String> ak = inputMessage.getHeaders().get(AuthConstants.HEADER_AK);
        if(ak!=null&&ak.size()==1){
            String sk=secretKeyStore.getSecretKey(ak.get(0));
            String body=String.join("\n", IOUtils.readLines(inputMessage.getBody()));
            body=aesEncryptor.decryptGCM(sk, body);
            return new DecryptHttpInputMessage(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)),inputMessage.getHeaders());
        }else {
            return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
        }
    }

    public static class DecryptHttpInputMessage implements HttpInputMessage{

        private InputStream body;
        private HttpHeaders httpHeaders;

        public DecryptHttpInputMessage(InputStream body, HttpHeaders httpHeaders) {
            this.body = body;
            this.httpHeaders = httpHeaders;
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return httpHeaders;
        }
    }
}
