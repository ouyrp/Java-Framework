package com.wusong.monitoring.metric.micrometer.binder.db;

import com.p6spy.engine.spy.P6DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.sql.DataSource;

public class P6DataSourceBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource && !(bean instanceof P6DataSource)) {
//            if("com.zaxxer.hikari.HikariDataSource".equals(bean.getClass().getName())){
//                // hikari直接转换为P6DataSource会导致hikari的处理逻辑出错，所以不转换HikariDataSource
//                return bean;
//            }
            P6DataSource dataSource = new P6DataSource((DataSource)bean);
            dataSource.setRealDataSource(beanName);
            return dataSource;
        } else {
            return bean;
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
