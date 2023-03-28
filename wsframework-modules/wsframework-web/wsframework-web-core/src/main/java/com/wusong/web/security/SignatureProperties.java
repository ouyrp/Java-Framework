package com.wusong.web.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author p14
 */
@ConfigurationProperties(prefix = "ws.signature")
@Data
public class SignatureProperties {
    private String path ="/api/**";
    private String excludePath="";
    private String salt="ws-default-salt";
}
