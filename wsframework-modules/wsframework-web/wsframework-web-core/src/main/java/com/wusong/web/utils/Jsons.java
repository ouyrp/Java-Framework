package com.wusong.web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * javadoc Jsons
 * <p>
 *     json相关util
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/31 15:59
 * @version 1.0.0
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Jsons {

    private static final ObjectMapper OM = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    /**
     * javadoc toJson
     * @apiNote to json string
     *
     * @param value any object
     * @return java.lang.String
     * @author weng xiaoyong
     * @date 2022/3/31 16:13
     **/
    public static String toJson(Object value){
        try {
            return OM.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            log.error("Jsons.toJson({}) exception: ", value, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * javadoc parseObject
     * @apiNote parse object by generics type reference
     *
     * @param json json string
     * @param reference generics type reference
     * @return T
     * @author weng xiaoyong
     * @date 2022/3/31 16:14
     **/
    public static <T> T parseObject(String json, TypeReference<T> reference){
        try {
            return OM.readValue(json, reference);
        } catch (JsonProcessingException ex) {
            log.error("Jsons.toJson({}, {}) exception: ", json, reference, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * javadoc parseObject
     * @apiNote parse object by object class type
     *
     * @param json json string
     * @param clazz class type
     * @return T
     * @author weng xiaoyong
     * @date 2022/3/31 16:14
     **/
    public static <T> T parseObject(String json, Class<T> clazz){
        if(Objects.isNull(json)){
            return null;
        }
        try {
            return OM.readValue(json, clazz);
        } catch (JsonProcessingException ex) {
            log.error("Jsons.toJson({}, {}) exception: ", json, clazz, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * javadoc parseObject
     * @apiNote parse object by reflect `Type`
     *          it always used in framework usually
     *
     * @param json json string
     * @param type reflect `Type`
     * @return T
     * @author weng xiaoyong
     * @date 2022/3/31 16:15
     **/
    public static <T> T parseObject(String json, Type type){
        if(Objects.isNull(json)){
            return null;
        }
        final JavaType javaType = OM.constructType(type);
        try {
            return OM.readValue(json, javaType);
        } catch (JsonProcessingException ex) {
            log.error("Jsons.toJson({}, {}-{}) exception: ", json, type, javaType, ex);
            throw new RuntimeException(ex);
        }
    }
}
