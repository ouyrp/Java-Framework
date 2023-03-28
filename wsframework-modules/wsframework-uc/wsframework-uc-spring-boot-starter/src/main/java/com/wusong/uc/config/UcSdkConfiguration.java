package com.wusong.uc.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@ConfigurationProperties(prefix = "wsframework.uc")
public class UcSdkConfiguration {

    /**
     * 应用接入的appId
     **/
    private String appId;

    /**
     * 拦截器权限校验的url匹配规则
     **/
    private String interceptorsPathPatterns = "/**";

    /**
     * 拦截器排除权限校验的url匹配规则
     **/
    private String interceptorsExcludePathPatterns;
}
