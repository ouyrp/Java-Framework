package com.wusong.uc.account.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc AccountRegisterSourceEnum
 * <p>
 *     账号注册来源枚举
 * <p>
 * @author weng xiaoyong
 * @date 2021/8/3 7:12 PM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum AccountRegisterSourceEnum {

    /**
     * 无讼案例
     * legal case
     **/
    LC("10010001"),

    /**
     * 天工
     * tian gong
     **/
    TG("10010002"),

    /**
     * 开务
     * kai wu
     **/
    KW("10010003"),

    /**
     * 学院微站
     **/
    COLLEGE_WAP("10010004"),

    /**
     * 律师服务平台
     **/
    LAWYER_PLATFORM("10010005"),

    /**
     * app
     **/
    APP("10010006"),

    /**
     * C 端
     **/
    MINI_PROGRAM_4C("10010007"),

    ;

    private final String source;
}
