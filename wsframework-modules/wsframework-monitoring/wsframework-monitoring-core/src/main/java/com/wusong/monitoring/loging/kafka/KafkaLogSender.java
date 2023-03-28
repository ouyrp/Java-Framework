package com.wusong.monitoring.loging.kafka;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wusong.monitoring.ApplicationProperties;
import org.apache.kafka.clients.producer.*;
import org.apache.skywalking.apm.toolkit.log.logback.v1.x.mdc.LogbackMDCPatternConverter;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author p14
 */
public class KafkaLogSender {

    private KafkaProperties kafkaProperties;
    private ApplicationProperties applicationProperties;
    private Properties props;

    public KafkaLogSender(KafkaProperties kafkaProperties, ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.kafkaProperties = kafkaProperties;
        props = new Properties();
        props.put("bootstrap.servers", kafkaProperties.getBootstrapServers());
        props.put("acks", kafkaProperties.getAcks());
        props.put("retries", kafkaProperties.getRetries());
        props.put("compression.type", kafkaProperties.getCompressionType());
        if (StringUtils.hasText(kafkaProperties.getBatchSize())) {
            props.put("batch.size", kafkaProperties.getBatchSize());
        }
        if (StringUtils.hasText(kafkaProperties.getBufferMemory())) {
            props.put("buffer.memory", kafkaProperties.getBufferMemory());
        }
        if (StringUtils.hasText(kafkaProperties.getLingerMills())) {
            props.put("linger.ms", kafkaProperties.getLingerMills());
        }
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }

    private Producer producer;
    private ExecutorService fallbackExecutor;
    private ThrowableProxyConverter tpc = new ThrowableProxyConverter();

    @PostConstruct
    public synchronized void start(){
        tpc.start();
        logbackMDCPatternConverter.start();
        producer = new KafkaProducer<>(props);
        if(fallbackExecutor!=null){
            fallbackExecutor.shutdown();
        }
        fallbackExecutor = Executors.newSingleThreadExecutor();
        KafkaAppender.setKafkaLogSender(this);
    }

    @PreDestroy
    public synchronized void stop(){
        tpc.stop();
        logbackMDCPatternConverter.stop();
        fallbackExecutor.shutdown();
        try {
            fallbackExecutor.awaitTermination(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fallbackExecutor=null;
    }

    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    private ObjectMapper objectMapper = new ObjectMapper();
    private LogbackMDCPatternConverter logbackMDCPatternConverter=new LogbackMDCPatternConverter();


    public void send(ILoggingEvent loggingEvent, BiConsumer<ILoggingEvent, Throwable> fallback) {
        HashMap<String, String> json = new HashMap<>();
        Object[] args = loggingEvent.getArgumentArray();
        String msg = loggingEvent.getMessage();
        if (args != null && args.length > 0) {
            msg = formatMessage(msg, args);
        }
        json.put("@timestamp", OffsetDateTime.ofInstant(new Date(loggingEvent.getTimeStamp()).toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        json.put("thread", loggingEvent.getThreadName());
        json.put("level", loggingEvent.getLevel().levelStr);
        json.put("logger", loggingEvent.getLoggerName());
        json.put("tid", logbackMDCPatternConverter.convertTID(loggingEvent));
        json.put("rid", loggingEvent.getMDCPropertyMap().get("reqId"));
        json.put("msg", msg+tpc.convert(loggingEvent));
        json.put("host", applicationProperties.getHostname());
        json.put("ip", applicationProperties.getIp());
        String app=System.getenv("APP");
        if(StringUtils.hasText(app)){
//            app=app.replaceAll("-","");
        }else {
            app=applicationProperties.getApplicationName();
        }
        json.put("app", app);
        String project=System.getenv("PROJECT");
        if(StringUtils.hasText(project)){
            json.put("project", project);
        }else {
            json.put("project", app);
        }
        json.put("env", applicationProperties.getProfile());
        try {
            Future future = producer.send(new ProducerRecord(kafkaProperties.getTopic(), objectMapper.writeValueAsString(json)));
            // 日志在独立单个线程中共调用 kafka 批量异步发送，不能直接调用 get ，否则会阻塞其它日志发送。
            // 所以另外启动一个线程监听发送kafka的结果，如果报错，则执行降级动作
            fallbackExecutor.execute(()->{
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    fallback.accept(loggingEvent,e);
                }
            });
        } catch (JsonProcessingException e) {
            fallback.accept(loggingEvent, e);
        }
    }

    public static String formatMessage(String message, Object... args) {
        if (args != null && args.length > 0) {
            StringBuilder formattedMessage = new StringBuilder();
            int argIndex = 0;
            for (int i = 0; i < message.length(); i++) {
                if (message.charAt(i) == DELIM_START) {
                    if (i + 1 < message.length() && message.charAt(i + 1) == DELIM_STOP) {
                        i++;
                        if (argIndex < args.length) {
                            if (args[argIndex] != null) {
                                formattedMessage.append(args[argIndex++]);
                            }else {
                                formattedMessage.append("null");
                            }
                        } else {
                            formattedMessage.append(DELIM_START).append(DELIM_STOP);
                        }
                    } else {
                        formattedMessage.append(message.charAt(i));
                    }
                } else {
                    formattedMessage.append(message.charAt(i));
                }
            }
            return formattedMessage.toString();
        }
        return message;
    }

}
