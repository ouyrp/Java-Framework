package com.wusong.monitoring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author p14
 */
@ConfigurationProperties("wsframework.monitoring")
@Data
public class MonitoringProperties {
    private String urlFilterPatterns ="/*";
    private String excludeAntPatterns="/static/**";
    /**
     * 是否打印所有日志
     */
    private boolean logAll =false;
    private boolean logHttpDetail =false;
    private String logPath="./logs";
    private String logbackPattern ="[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%thread] [%-5level] [%logger{40}] [%X{tid}] - %X{reqId} %msg%n";
    private int maxLogLength=5000;

}
