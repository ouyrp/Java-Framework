package com.wusong.web.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

/**
 * 业务异常。service中抛出这个异常
 * @author p14
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class BusinessException extends RuntimeException {
    private Error error;

    public BusinessException(String message) {
        super(message);
        this.error = Errors.SINGLE_MESSAGE.format(message);
    }
    public BusinessException(String message, Throwable cause) {
        super(message,cause);
        this.error = Errors.SINGLE_MESSAGE.format(message).setCause(cause);
    }

    public BusinessException(String code, String message){
        super(StringUtils.hasLength(message)?message:code);
        this.error=Errors.error(code,message);
    }
    public BusinessException(String code,String message,Throwable cause){
        super(StringUtils.hasLength(message)?message:code,cause);
        this.error=Errors.error(code,message).setCause(cause);
    }

    public BusinessException(Error error) {
        super(error.getMessage());
        Objects.nonNull(error);
        this.error = error;
    }

    public BusinessException(Error error, Throwable cause) {
        super(error.getMessage(),cause);
        Objects.nonNull(error);
        this.error = error;
    }

    public static void main(String[] args) {
        System.out.println(OffsetDateTime.now().plusDays(2).until(OffsetDateTime.now().plusDays(1), ChronoUnit.SECONDS));
    }

}
