package com.wusong.uc.account.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc AccountSystemEnum
 * <p>
 *     账户系统code枚举
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/25 11:08 AM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum AccountSystemEnum {

    /**
     * 内部用户
     **/
    INTERNAL("internal"),

    /**
     * 外部用户
     **/
    EXTERNAL("external"),
    ;

    private final String code;
}
