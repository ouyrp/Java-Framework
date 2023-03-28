package com.wusong.web.security.simple;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

/**
 * @author p14
 */
@ConfigurationProperties(prefix = "ws.signature.simple")
@Data
public class SimpleSecretKeyProperties {
    private HashMap<String,SecretKey> secret;

    @Data
    public static class SecretKey{
        private String sk;
    }
}
