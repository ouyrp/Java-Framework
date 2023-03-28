package com.wusong.monitoring.metric.micrometer.autoconfigure;

import com.wusong.monitoring.metric.micrometer.PlatformTag;
import com.wusong.monitoring.metric.micrometer.binder.micrometer.MicrometerBinder;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

public class MicrometerAutoConfiguration {


    @Bean
    public MyRegistryCustomizer meterRegistryCustomizer(PlatformTag platformTag) {
        MyRegistryCustomizer ret = new MyRegistryCustomizer(platformTag);
        return ret;
    }

    @Bean
    public MicrometerBinder micrometerBinder() {
        return new MicrometerBinder();
    }

//    @Bean
//    public InetUtils monitorPinetUtils(InetUtilsProperties properties) {
//        return new InetUtils(properties);
//    }
//
//    @Bean
//    public InetUtilsProperties monitorProperties() {
//        return new InetUtilsProperties();
//    }

    @Bean
    public PlatformTag platformTag() {
        return new PlatformTag();
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
//
//    @Bean
//    public EnvBinder envBinder(){
//        return new EnvBinder();
//    }
}
