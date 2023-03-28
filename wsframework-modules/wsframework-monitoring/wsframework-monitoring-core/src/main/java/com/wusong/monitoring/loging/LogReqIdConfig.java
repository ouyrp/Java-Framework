package com.wusong.monitoring.loging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import javax.servlet.Filter;
import java.util.Arrays;

/**
 * @author p14
 */
@ConditionalOnClass(Filter.class)
public class LogReqIdConfig {

    @Bean
    @ConditionalOnProperty(name = "wsframework.monitoring.log.reqId.enable",havingValue = "true",matchIfMissing = true)
    public FilterRegistrationBean<ReqIdFilter> reqIdFilterFilterRegistrationBean(){
        FilterRegistrationBean<ReqIdFilter> bean=new FilterRegistrationBean<>();
        bean.setFilter(new ReqIdFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }
}
