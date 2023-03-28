package com.wusong.monitoring.loging.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author p14
 */
@Data
@ConfigurationProperties(prefix = "logging.kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private String acks="1";
    private String retries="2";
    private String batchSize;
    private String lingerMills="5";
    private String bufferMemory;
    private String topic="OPT_LOGS";
    private String compressionType="none";
}
