package com.wusong.monitoring.metric.micrometer.autoconfigure;

import com.p6spy.engine.spy.P6DataSource;
import com.wusong.monitoring.metric.micrometer.MonitorConfigSpringApplicationRunListener;
import com.wusong.monitoring.metric.micrometer.binder.db.DruidDataSourcePoolMetadata;
import com.wusong.monitoring.metric.micrometer.binder.db.P6DataSourceBeanPostProcessor;
import com.wusong.monitoring.metric.micrometer.binder.db.p6spy.RecodeLoggingEventListener;
import com.wusong.monitoring.metric.micrometer.binder.db.p6spy.SQLLogger;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.HikariDataSourcePoolMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.List;

import static com.wusong.monitoring.metric.micrometer.MonitorConfigSpringApplicationRunListener.CONNECTION_CREATE_THRESHOLD;


@AutoConfigureAfter({MicrometerAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class DataSourceMetricAutoConfiguration {

    @Configuration
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)
    static class DruidDataSourceMetricAutoConfiguration {
        @Bean
        public DataSourcePoolMetadataProvider dataSourcePoolMetadataProvider() {
            return new MetricDataSourcePoolMetadataProvider();
        }
    }

    public static class MetricDataSourcePoolMetadataProvider implements DataSourcePoolMetadataProvider {

        @Override
        public DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource) {
            if (dataSource instanceof com.alibaba.druid.pool.DruidDataSource) {
                return new DruidDataSourcePoolMetadata((com.alibaba.druid.pool.DruidDataSource)dataSource);
            } else {
                if (dataSource instanceof P6DataSource) {
                    Field realDataSourceField = ReflectionUtils.findField(P6DataSource.class, "realDataSource");
                    ReflectionUtils.makeAccessible(realDataSourceField);
                    try {
                        CommonDataSource realDataSource = (CommonDataSource)realDataSourceField.get(dataSource);
                        if (realDataSource instanceof com.alibaba.druid.pool.DruidDataSource) {
                            return new DruidDataSourcePoolMetadata((com.alibaba.druid.pool.DruidDataSource)realDataSource);
                        }
                    } catch (IllegalAccessException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                return null;
            }
        }
    }

    @Configuration
    @ConditionalOnClass(HikariDataSource.class)
    static class HikariPoolDataSourceMetadataProviderConfiguration {

        @Bean
        public DataSourcePoolMetadataProvider hikariPoolDataSourceMetadataProvider() {
            return (dataSource) -> {
                HikariDataSource hikariDataSource = DataSourceUnwrapper.unwrap(dataSource,
                        HikariDataSource.class);
                if (hikariDataSource != null) {
                    return new HikariDataSourcePoolMetadata(hikariDataSource);
                }
                return null;
            };
        }

    }

    public static final Logger logger = LoggerFactory.getLogger(DataSourceMetricAutoConfiguration.class);

}
