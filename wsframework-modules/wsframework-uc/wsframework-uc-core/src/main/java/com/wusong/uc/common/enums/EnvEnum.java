package com.wusong.uc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc EnvEnum
 * <p>
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/25 4:19 PM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum EnvEnum {

    DEV("DEV"),

    TEST("TEST"),

    PROD("PROD"),
    ;

    private final String env;
}
