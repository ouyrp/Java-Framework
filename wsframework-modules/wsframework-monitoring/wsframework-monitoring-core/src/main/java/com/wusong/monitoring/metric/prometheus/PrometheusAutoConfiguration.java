package com.wusong.monitoring.metric.prometheus;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
public class PrometheusAutoConfiguration {

    public PrometheusActuatorEndpoint prometheusActuatorEndpoint(PrometheusMeterRegistry prometheusMeterRegistry) {
        return new PrometheusActuatorEndpoint(prometheusMeterRegistry);
    }

}
