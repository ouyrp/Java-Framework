package com.wusong.uc.common.exception;


import com.wusong.uc.common.enums.AuthorizationCodeEnum;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc AuthorizationException
 * <p>
 *     认证异常
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/16 5:04 PM
 * @version 1.0.0
 **/
@ToString(callSuper = true)
@Getter
public class AuthorizationException extends RuntimeException{

    private final String code;

    public AuthorizationException(String code, String message){
        super(message);
        this.code = code;
    }

    public AuthorizationException(AuthorizationCodeEnum code){
        super(code.getMessage());
        this.code = code.getCode();
    }
}
