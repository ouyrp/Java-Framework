package com.wusong.uc.account.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc AccountProductLineEnum
 * <p>
 *     账号所属产品线枚举
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/4 11:20 AM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum AccountProductLineEnum {

    /**
     * 平台端
     **/
    PLATFORM("P"),

    /**
     * 律师端
     **/
    LAWYER("L"),

    /**
     * C 端
     **/
    CUSTOMER("C"),

    /**
     * 数据部门
     **/
    DATA("D"),

    ;

    private final String line;

}
