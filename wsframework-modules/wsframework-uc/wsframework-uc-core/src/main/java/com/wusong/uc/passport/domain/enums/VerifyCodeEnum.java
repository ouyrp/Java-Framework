package com.wusong.uc.passport.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc VerifyCodeEnum
 * <p>
 *     验证码类型枚举
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 7:24 PM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum VerifyCodeEnum {

    /**
     * 用于登录
     **/
    LOGIN("LOGIN"),

    /**
     * 用于修改密码
     **/
    UPDATE_PASSWORD("UPDATE_PASSWORD"),

    /**
     * 注册
     **/
    REGISTER("REGISTER"),

    /**
     * 修改登录账号
     **/
    CHANGE_ACCOUNT_NAME("CHANGE_ACCOUNT_NAME"),
    ;

    private final String type;
}
