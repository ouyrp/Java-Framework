package com.wusong.monitoring.loging.kafka;

import com.wusong.monitoring.ApplicationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author p14
 */
@Configuration
@ConditionalOnProperty(name = "logging.kafka.bootstrapServers")
@Import(KafkaProperties.class)
public class KafkaConfiguration {

    @Bean
    public KafkaLogSender kafkaLogSender(ApplicationProperties applicationProperties,KafkaProperties kafkaProperties){
        KafkaLogSender sender = new KafkaLogSender(kafkaProperties, applicationProperties);
        return sender;
    }
}
