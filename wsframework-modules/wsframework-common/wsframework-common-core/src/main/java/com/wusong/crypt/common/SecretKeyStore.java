package com.wusong.crypt.common;

/**
 * @author p14
 */
public interface SecretKeyStore {
    String getSecretKey(String accessKey);
}
