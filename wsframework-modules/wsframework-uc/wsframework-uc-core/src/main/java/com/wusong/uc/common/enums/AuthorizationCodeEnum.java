package com.wusong.uc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc AuthorizationCodeEnum
 * <p>
 *     几种常见的认证异常code message
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/17 10:10 AM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum AuthorizationCodeEnum {

    UN_AUTH("-1", "未认证"),

    AUTH_EXPIRE("-2", "授权过期"),

    AUTH_INVALID("-3", "授权无效"),

    AUTH_NOT_EXIST("-4", "授权不存在"),


    ;

    private final String code;

    private final String message;


}
