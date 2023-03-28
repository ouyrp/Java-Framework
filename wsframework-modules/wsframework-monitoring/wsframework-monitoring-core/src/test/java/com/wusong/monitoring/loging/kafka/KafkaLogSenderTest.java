package com.wusong.monitoring.loging.kafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author p14
 */
class KafkaLogSenderTest {

    @Test
    void init() {
        Assertions.assertEquals("",KafkaLogSender.formatMessage("",null,null));
        Assertions.assertEquals("a",KafkaLogSender.formatMessage("a",null,null));
        Assertions.assertEquals("a ",KafkaLogSender.formatMessage("a {}",null,null));
        Assertions.assertEquals("a }",KafkaLogSender.formatMessage("a }",null,null));
        Assertions.assertEquals("a {",KafkaLogSender.formatMessage("a {",null,null));
        Assertions.assertEquals("a {",KafkaLogSender.formatMessage("a {",1,2));
        Assertions.assertEquals("a 1",KafkaLogSender.formatMessage("a {}",1,2));
        Assertions.assertEquals("a 1{2}",KafkaLogSender.formatMessage("a {}{{}}",1,2));
        Assertions.assertEquals("测试撒发顺丰 【】[s] --给",KafkaLogSender.formatMessage("测试撒发顺丰 【】[{}] --{}","s","给"));
    }
}