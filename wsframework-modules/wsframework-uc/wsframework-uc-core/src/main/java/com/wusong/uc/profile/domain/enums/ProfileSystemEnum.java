package com.wusong.uc.profile.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc UserSystemEnum
 * <p>
 *     用户系统枚举
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/4 12:14 PM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum ProfileSystemEnum {

    /**
     * 用户中心
     **/
    UC("UC"),

    /**
     * 天工
     **/
    TG("TG"),

    /**
     * 案例
     **/
    CASE("CASE"),

    /**
     * 阅读
     **/
    READER("READER"),

    /**
     * 论剑
     **/
    SWORD("SWORD"),
    ;

    private final String system;
}
