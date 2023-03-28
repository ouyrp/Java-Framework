package com.wusong.web.security.simple;

import com.wusong.crypt.common.SecretKeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author p14
 */
@ConditionalOnMissingBean(SecretKeyStore.class)
@Import(SimpleSecretKeyProperties.class)
public class SimpleSecretKeyStoreAutoConfiguration {
    @Autowired
    private SimpleSecretKeyProperties keyProperties;
    @Bean
    @ConditionalOnMissingBean(SecretKeyStore.class)
    public SecretKeyStore simpleSecretKeyStore(){
        return new SimpleSecretKeyStore(keyProperties);
    }
}
