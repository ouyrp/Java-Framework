package com.wusong.monitoring.metric.micrometer.autoconfigure;

import com.wusong.monitoring.metric.micrometer.PlatformTag;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfigureAfter(MicrometerAutoConfiguration.class)
public class JVMAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ClassLoaderMetrics classLoaderMetrics(PlatformTag platformTag) {
        return new ClassLoaderMetrics(platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public JvmGcMetrics jvmGcMetrics(PlatformTag platformTag) {
        return new JvmGcMetrics(platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public JvmMemoryMetrics jvmMemoryMetrics(PlatformTag platformTag) {
        return new JvmMemoryMetrics(platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public JvmThreadMetrics jvmThreadMetrics(PlatformTag platformTag) {
        return new JvmThreadMetrics(platformTag.getTags());
    }
}
