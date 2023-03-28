package com.wusong.web.exception;

import com.wusong.web.dto.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * @author p14
 */
@RestControllerAdvice
@Slf4j
public class WsFrameworkGlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResult<?> globalException(Exception exception, HttpServletResponse response){
        if(exception instanceof ExceptionWithoutErrorLog){
            log.trace("unhandled Exception ",exception);
        }else {
            log.error("unhandled Exception ",exception);
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ApiResult.error(Errors.error(HttpStatus.INTERNAL_SERVER_ERROR.value()+"", exception.getMessage()!=null?exception.getMessage():exception.getClass().toString()));
    }

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ApiResult<?> businessException(BusinessException exception, HttpServletResponse response){
        if(exception instanceof ExceptionWithoutErrorLog){
            log.trace("unhandled BusinessException ",exception);
        }else {
            log.error("unhandled BusinessException ",exception);
        }
        response.setStatus(exception.getError().getHttpCode());
        return ApiResult.error(exception.getError().getCode(), exception.getMessage()!=null?exception.getMessage():exception.getClass().toString()).setData(exception.getError().getResponseBody());
    }
}
