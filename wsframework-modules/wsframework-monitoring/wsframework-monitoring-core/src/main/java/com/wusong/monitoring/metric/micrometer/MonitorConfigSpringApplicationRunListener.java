package com.wusong.monitoring.metric.micrometer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 该类必须在ConfigCenter的ApolloSpringApplicationRunListener之后
 */
@Order(value = 2)
@Slf4j
public class MonitorConfigSpringApplicationRunListener implements SpringApplicationRunListener {
    public MonitorConfigSpringApplicationRunListener(SpringApplication application, String[] args) {
    }

    public static final String METRICS_INFLUX_TIMER_PERCENTILES = "management.metric.export.timer-percentiles";
    public static final String METRICS_INFLUX_CLEANER_TRIGGER = "management.metric.export.cleaner-trigger";
    public static final String METRICS_INFLUX_TAGS_IGNORE = "management.metric.export.tags-ignore";

    public static final String CONNECTION_CREATE_THRESHOLD = "wsframework.monitoring.connection-create-threshold";

    private static ConfigurableEnvironment ENVIRONMENT;

    public static final String getConfig(String key, String defaultValue) {
        if(ENVIRONMENT.containsProperty(key.toUpperCase())){
            return ENVIRONMENT.getProperty(key.toUpperCase());
        }

        if(ENVIRONMENT.containsProperty(key)){
            return ENVIRONMENT.getProperty(key);
        }
        log.info("Cannot found " + key + " in Environment. Using defaultValue " +defaultValue);
        return defaultValue;
    }

    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        MonitorConfigSpringApplicationRunListener.ENVIRONMENT = environment;
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }

}
