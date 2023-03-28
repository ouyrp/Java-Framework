package com.wusong.monitoring.loging.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Iterator;

/**
 * @author p14
 */
@Data
public class KafkaAppender extends AppenderBase<ILoggingEvent> implements AppenderAttachable<ILoggingEvent> {

    private static KafkaLogSender kafkaLogSender;

    public static KafkaLogSender getKafkaLogSender() {
        return kafkaLogSender;
    }

    public static void setKafkaLogSender(KafkaLogSender kafkaLogSender) {
        KafkaAppender.kafkaLogSender = kafkaLogSender;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if(preferKafka&&!getName().endsWith("_ignore") && getKafkaLogSender()!=null){
            sendKafka(eventObject);
        }else {
            if(!preferKafka){
                logCounter++;
                if(logCounter>10){
                    logCounter=-1;
                    preferKafka=true;
                    System.out.println(LocalDateTime.now() +" 重新启用kafka logger");
                }
            }
            fallback(eventObject);
        }
    }

    /**
     * 记录kafka发送错误次数，出错后来临时熔断，10次内不发送kafka。
     */
    private int logCounter=0;
    private boolean preferKafka=true;

    private void sendKafka(ILoggingEvent eventObject) {
        try{
            getKafkaLogSender().send(eventObject,(e,t)->{
                preferKafka=false;
                System.err.println(LocalDateTime.now() +" 发送 kafka logger 错误");
                t.printStackTrace();
                fallback(eventObject);
            });
        }catch (Throwable t){
            fallback(eventObject);
        }
    }

    private void fallback(ILoggingEvent eventObject){
        aai.iteratorForAppenders().forEachRemaining(appenderAppender -> {
            appenderAppender.doAppend(eventObject);
        });
    }

    private AppenderAttachableImpl<ILoggingEvent> aai=new AppenderAttachableImpl<>();

    @Override
    public void addAppender(Appender<ILoggingEvent> newAppender) {
        aai.addAppender(newAppender);
    }

    @Override
    public Iterator<Appender<ILoggingEvent>> iteratorForAppenders() {
        return aai.iteratorForAppenders();
    }

    @Override
    public Appender<ILoggingEvent> getAppender(String name) {
        return aai.getAppender(name);
    }

    @Override
    public boolean isAttached(Appender<ILoggingEvent> appender) {
        return aai.isAttached(appender);
    }

    @Override
    public void detachAndStopAllAppenders() {
        aai.detachAndStopAllAppenders();
    }

    @Override
    public boolean detachAppender(Appender<ILoggingEvent> appender) {
        return aai.detachAppender(appender);
    }

    @Override
    public boolean detachAppender(String name) {
        return aai.detachAppender(name);
    }
}
