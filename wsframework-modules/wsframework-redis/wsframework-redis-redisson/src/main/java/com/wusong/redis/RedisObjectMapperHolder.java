package com.wusong.redis;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author p14
 */
public class RedisObjectMapperHolder {
    private ObjectMapper objectMapper;

    public RedisObjectMapperHolder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
