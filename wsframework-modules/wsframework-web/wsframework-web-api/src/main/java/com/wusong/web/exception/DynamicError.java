package com.wusong.web.exception;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author p14
 */
@Data
@Accessors(chain = true)
public class DynamicError implements Error {
    private String code;
    private String message;
    private int httpCode=200;
    private Object responseBody;
    private Throwable cause;
}
