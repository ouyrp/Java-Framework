package com.wusong.monitoring;

import com.wusong.monitoring.aspect.ControllerAspect;
import com.wusong.monitoring.aspect.MonitoringAspect;
import com.wusong.monitoring.aspect.RestControllerAspect;
import com.wusong.monitoring.loging.LogFilter;
import com.wusong.monitoring.loging.LogReqIdConfig;
import com.wusong.monitoring.loging.LogbackConfiguration;
import com.wusong.monitoring.loging.kafka.KafkaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.servlet.Filter;
import java.util.Arrays;

/**
 * @author p14
 */
@ConditionalOnProperty(name = "wsframework.monitoring.enable",havingValue = "true",matchIfMissing = true)
@Import({ApplicationProperties.class,MonitoringProperties.class,LogReqIdConfig.class, KafkaConfiguration.class})
public class MonitorAutoConfiguration {

    @Autowired
    private MonitoringProperties monitoringProperties;

    @Bean
    @ConditionalOnProperty(name = "wsframework.monitoring.log.enable",havingValue = "true",matchIfMissing = true)
    public MonitoringAspect monitorAspect(MonitoringProperties monitoringProperties){
        return new MonitoringAspect(monitoringProperties);
    }

    @Bean
    @ConditionalOnProperty(name = "wsframework.monitoring.log.controller.enable",havingValue = "true",matchIfMissing = true)
    public ControllerAspect controllerAspect(MonitoringProperties monitoringProperties){
        return new ControllerAspect(monitoringProperties);
    }

    @Bean
    @ConditionalOnProperty(name = "wsframework.monitoring.log.rest.enable",havingValue = "true",matchIfMissing = true)
    public RestControllerAspect restControllerAspect(MonitoringProperties monitoringProperties){
        return new RestControllerAspect(monitoringProperties);
    }

    @Bean
    @ConditionalOnProperty(name = "wsframework.monitoring.log.filter.enable",havingValue = "true",matchIfMissing = true)
    public FilterRegistrationBean<LogFilter> logFilter(){
        FilterRegistrationBean<LogFilter> bean=new FilterRegistrationBean<>();
        bean.setFilter(new LogFilter(monitoringProperties));
        bean.setOrder(-1);
        bean.setUrlPatterns(Arrays.asList(monitoringProperties.getUrlFilterPatterns().split(",")));
        return bean;
    }

    @Bean
    public LogbackConfiguration logbackConfiguration(){
        return new LogbackConfiguration();
    }

}
