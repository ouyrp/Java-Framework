package com.wusong.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wusong.crypt.common.SecretKeyAuthenticator;
import com.wusong.crypt.common.SecretKeyStore;
import com.wusong.crypt.web.SignatureHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author p14
 */
public class SignatureWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecretKeyStore secretKeyStore;
    @Autowired(required = false)
    private SecretKeyAuthenticator secretKeyAuthenticator;
    @Autowired
    private SignatureProperties signatureProperties;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SignatureHandlerInterceptor(objectMapper,secretKeyStore,secretKeyAuthenticator))
                .order(-1)
                .addPathPatterns(signatureProperties.getPath().split(","))
                .excludePathPatterns(signatureProperties.getExcludePath().split(","));
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
