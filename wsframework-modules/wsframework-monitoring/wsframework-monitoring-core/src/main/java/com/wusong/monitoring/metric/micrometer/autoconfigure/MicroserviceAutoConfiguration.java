package com.wusong.monitoring.metric.micrometer.autoconfigure;

import com.wusong.monitoring.metric.micrometer.PlatformTag;
import com.wusong.monitoring.metric.micrometer.binder.openfeign.OpenfeignMetricsBinder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureAfter(value = {MicrometerAutoConfiguration.class})
@ConditionalOnClass(FeignClient.class)
public class MicroserviceAutoConfiguration {

    @Configuration
    @ConditionalOnClass(FeignClient.class)
    static class FeigMeternAutoConfiguration {
        @Bean
        public OpenfeignMetricsBinder openfeignMetrics(PlatformTag platformTag) {
            return new OpenfeignMetricsBinder(platformTag.getTags());
        }
    }

}
