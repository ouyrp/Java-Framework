package com.wusong.monitoring.metric.prometheus;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerEndpoint(id = "prometheus")
public class PrometheusActuatorEndpoint {
    private PrometheusMeterRegistry prometheusMeterRegistry;

    public PrometheusActuatorEndpoint(PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    @ResponseBody
    @RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String writeMetrics(HttpServletResponse response) throws IOException {
        return prometheusMeterRegistry.scrape();
    }
}
