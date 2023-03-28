package com.wusong.web.security.simple;

import com.wusong.crypt.common.SecretKeyStore;
import com.wusong.web.exception.BusinessException;

/**
 * @author p14
 */
public class SimpleSecretKeyStore implements SecretKeyStore {
    private SimpleSecretKeyProperties secretKeyProperties;

    public SimpleSecretKeyStore(SimpleSecretKeyProperties secretKeyProperties) {
        this.secretKeyProperties = secretKeyProperties;
    }

    @Override
    public String getSecretKey(String accessKey) {
        if(secretKeyProperties.getSecret().containsKey(accessKey)){
            return secretKeyProperties.getSecret().get(accessKey).getSk();
        }else {
            throw new BusinessException("accessKey 不存在 "+accessKey);
        }
    }
}
