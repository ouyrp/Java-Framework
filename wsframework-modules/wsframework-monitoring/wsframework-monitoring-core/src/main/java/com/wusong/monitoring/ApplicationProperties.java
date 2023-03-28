package com.wusong.monitoring;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * @author p14
 */
@Data
@Configuration
public class ApplicationProperties {
    public ApplicationProperties() {
        try {
            this.hostname= Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            this.hostname="";
        }
        try {
            this.ip= Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            this.ip="";
        }
    }

    @Value("${spring.application.name}")
    String applicationName;

    String hostname;
    String ip;

    @Value("${spring.profiles.active:}")
    String profile;

}
