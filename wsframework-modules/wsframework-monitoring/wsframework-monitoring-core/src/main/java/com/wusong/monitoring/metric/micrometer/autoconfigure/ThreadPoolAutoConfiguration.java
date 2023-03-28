package com.wusong.monitoring.metric.micrometer.autoconfigure;

import com.wusong.monitoring.metric.micrometer.binder.threadpool.ThreadPoolBinder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

@AutoConfigureAfter(MicrometerAutoConfiguration.class)
public class ThreadPoolAutoConfiguration {

    @Bean
    public ThreadPoolBinder threadPoolBinder() {
        return new ThreadPoolBinder();
    }
}
