package com.wusong.monitoring.loging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AsyncAppenderBase;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.AppenderAttachable;
import com.wusong.monitoring.MonitoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.log.logback.v1.x.mdc.TraceIdMDCPatternLogbackLayout;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author p14
 */
@Slf4j
public class LogbackConfiguration {

    @Autowired
    private Environment env;
    @Autowired
    private MonitoringProperties monitoringProperties;

    @PostConstruct
    public void init() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        if (loggerContext == null) {
            log.error("LoggerContext is null");
            return;
        }
        List<Logger> loggerList = loggerContext.getLoggerList();
        for (Logger logger : loggerList) {
            setLayout(loggerContext,logger.iteratorForAppenders());
        }
        MaxLengthMessageConverter.setMaxLength(monitoringProperties.getMaxLogLength());
    }

    private void setLayout(LoggerContext loggerContext, Iterator<? extends Appender<?>> iterator){
        while (iterator.hasNext()) {
            Appender<?> appender = iterator.next();
            setAppenderLayout(loggerContext,appender);
        }
    }

    private void setAppenderLayout(LoggerContext loggerContext, Appender<?> appender){
        if (appender instanceof OutputStreamAppender) {
            setLayout(loggerContext,(OutputStreamAppender<?>)appender);
        } else if(appender instanceof AppenderAttachable){
            AppenderAttachable<?> asyncAppenderBase= (AppenderAttachable<?>) appender;
            setLayout(loggerContext, asyncAppenderBase.iteratorForAppenders());
        }
    }


    private void setLayout(LoggerContext loggerContext,OutputStreamAppender<?> outputStreamAppender){
        Encoder<?> encoder = outputStreamAppender.getEncoder();
        if (encoder instanceof LayoutWrappingEncoder) {
            TraceIdMDCPatternLogbackLayout traceIdLayOut = new TraceIdMDCPatternLogbackLayout();
            traceIdLayOut.setContext(loggerContext);
            traceIdLayOut.setPattern(monitoringProperties.getLogbackPattern());
            traceIdLayOut.start();
            Field field = ReflectionUtils.findField(encoder.getClass(), "layout");
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, encoder, traceIdLayOut);
        }
    }

}
