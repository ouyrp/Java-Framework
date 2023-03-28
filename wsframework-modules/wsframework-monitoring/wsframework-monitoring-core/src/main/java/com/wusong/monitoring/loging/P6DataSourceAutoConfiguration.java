package com.wusong.monitoring.loging;

import com.p6spy.engine.spy.P6DataSource;
import com.wusong.monitoring.metric.micrometer.MonitorConfigSpringApplicationRunListener;
import com.wusong.monitoring.metric.micrometer.binder.db.P6DataSourceBeanPostProcessor;
import com.wusong.monitoring.metric.micrometer.binder.db.p6spy.RecodeLoggingEventListener;
import com.wusong.monitoring.metric.micrometer.binder.db.p6spy.SQLLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.List;

import static com.wusong.monitoring.metric.micrometer.MonitorConfigSpringApplicationRunListener.CONNECTION_CREATE_THRESHOLD;

/**
 * @author p14
 */
@ConditionalOnBean(DataSource.class)
@ConditionalOnClass(com.p6spy.engine.spy.P6DataSource.class)
@Slf4j
public class P6DataSourceAutoConfiguration {
    public static final String APPENDER = "p6spy.config.appender";

    public static final String CUSTOM_LOG_MESSAGE_FORMAT = "p6spy.config.customLogMessageFormat";

    public static final String LOG_MESSAGE_FORMAT = "p6spy.config.logMessageFormat";
    public static final String EXECUTION_THRESHOLD = "p6spy.config.executionThreshold";
    public static final String OUTAGE_DETECTION = "p6spy.config.outagedetection";
    public static final String OUTAGE_DETECTION_INTERVAL = "p6spy.config.outagedetectioninterval";
    static {

        System.setProperty(APPENDER, "com.wusong.monitoring.metric.micrometer.binder.db.p6spy.SQLLogger");
        System.setProperty(CUSTOM_LOG_MESSAGE_FORMAT,
                "%(executionTime)ms|%(category)|connection%(connectionId)|%(effectiveSqlSingleLine)");
        System.setProperty(LOG_MESSAGE_FORMAT, "com.p6spy.engine.spy.appender.CustomLineFormat");
        System.setProperty(OUTAGE_DETECTION, "true");

        boolean showSqlWithRealParameter = Boolean.parseBoolean(
                MonitorConfigSpringApplicationRunListener.getConfig("wsframework.monitoring.sql.with-real-parameter", "false"));

        int executionThreshold = Integer
                .parseInt(MonitorConfigSpringApplicationRunListener.getConfig("wsframework.monitoring.sql.executionthreshold", "0"));;

        int outagedetectioninterval = Integer.parseInt(
                MonitorConfigSpringApplicationRunListener.getConfig("wsframework.monitoring.sql.outagedetectioninterval", "5"));;

        if (showSqlWithRealParameter) {
            System.setProperty(CUSTOM_LOG_MESSAGE_FORMAT,
                    "%(executionTime)ms|%(category)|connection%(connectionId)|%(sqlSingleLine)");
        }
        if (executionThreshold > 0) {
            System.setProperty(EXECUTION_THRESHOLD, String.valueOf(executionThreshold));
        } else {
            System.setProperty(EXECUTION_THRESHOLD, String.valueOf(0));
        }
        if (outagedetectioninterval > 0) {
            System.setProperty(OUTAGE_DETECTION_INTERVAL, String.valueOf(outagedetectioninterval));
        } else {
            System.setProperty(EXECUTION_THRESHOLD, String.valueOf(5));
        }
        RecodeLoggingEventListener.setConnectionCreateThreshold(
                Integer.parseInt(MonitorConfigSpringApplicationRunListener.getConfig(CONNECTION_CREATE_THRESHOLD,"10")));
    }

    @Bean
    public P6DataSourceBeanPostProcessor dataSourceBeanPostProcessor() {
        return new P6DataSourceBeanPostProcessor();
    }

    @Bean
    @ConditionalOnBean(DataSourcePoolMetadataProvider.class)
    public DataSourcePoolMetadataProvider
    p6DataSourcePoolMetadataProvider(List<DataSourcePoolMetadataProvider> allDataSourcePoolMetadataProvider) {
        return new DataSourcePoolMetadataProvider() {
            @Override
            public DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource) {
                if (dataSource instanceof P6DataSource) {
                    Field realDataSourceField = ReflectionUtils.findField(P6DataSource.class, "realDataSource");
                    ReflectionUtils.makeAccessible(realDataSourceField);
                    try {
                        CommonDataSource ds = (CommonDataSource)realDataSourceField.get(dataSource);
                        if (ds instanceof DataSource) {
                            dataSource = (DataSource)ds;
                            log.info("realDataSource is {}", ds.getClass());
                        }
                    } catch (IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                for (DataSourcePoolMetadataProvider d : allDataSourcePoolMetadataProvider) {
                    DataSourcePoolMetadata dataSourcePoolMetadata = d.getDataSourcePoolMetadata(dataSource);
                    if (null != dataSourcePoolMetadata) {
                        return dataSourcePoolMetadata;
                    }
                }
                return null;
            }
        };
    }

    @Bean
    public SQLLogger sqlLogger() {
        return new SQLLogger();
    }
}
