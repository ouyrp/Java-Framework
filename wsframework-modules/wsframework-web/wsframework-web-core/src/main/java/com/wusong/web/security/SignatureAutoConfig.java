package com.wusong.web.security;

import com.wusong.crypt.common.AESEncryptor;
import com.wusong.crypt.web.SignatureRequestWrapper;
import com.wusong.web.security.simple.SimpleSecretKeyStoreAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

/**
 * @author p14
 */
@Import({SignatureProperties.class, SimpleSecretKeyStoreAutoConfiguration.class})
public class SignatureAutoConfig {

    @Autowired
    private SignatureProperties signatureProperties;

    @Bean
    public SignatureWebMvcConfigurer signatureWebMvcConfigurer(){
        return new SignatureWebMvcConfigurer();
    }

    /**
     * 保留 request body，供后面验签
     * @return
     */
    @Bean
    public FilterRegistrationBean<SignatureRequestWrapper.HttpServletFilter> filterRegistrationBean(){
        FilterRegistrationBean<SignatureRequestWrapper.HttpServletFilter> bean=new FilterRegistrationBean<>();
        bean.setFilter(new SignatureRequestWrapper.HttpServletFilter());
        // order 需要靠前一些。在body解密之前做验签
        bean.setName("signatureFilter");
        bean.setUrlPatterns(Arrays.asList("/*"));
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public AESEncryptor aesEncryptor(){
        return new AESEncryptor(signatureProperties.getSalt());
    }


    @Bean
    public DecryptRequestBody decryptRequestBody(){
        return new DecryptRequestBody();
    }
    @Bean
    public EncryptResponseBody encryptResponseBody(){
        return new EncryptResponseBody();
    }

}
