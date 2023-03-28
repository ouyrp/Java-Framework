package com.wusong.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author p14
 */
@Data
@ConfigurationProperties(prefix = "wsframework.okhttp")
public class HttpProperties {
    /**
     * 保持和 tomcat 默认线程数一致
     */
    private int maxIdle=200;
    /**
     * 10s 不使用的连接就释放掉，防止断连太多导致线程数飙升不释放
     */
    private long keepAliveSeconds=10;
    /**
     * 保持和 tomcat 默认线程数一致
     */
    private int maxRequests=200;
    /**
     * 保持和 tomcat 默认线程数一致
     */
    private int maxRequestsPerHost=200;
    /**
     * connect 一般非常快。除非dns解析满或者网络差可能导致 connect 超时
     */
    private int connectTimeoutMills=3000;
    /**
     * 保持和 nginx 默认值一致
     */
    private int readTimeoutMills=60_000;
    /**
     * 一般写不需要超时（比如上传文件不能超时）
     */
    private int writeTimeoutMills=0;
}
