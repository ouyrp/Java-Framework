package com.wusong.uc.account.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc AccountTypeEnum
 * <p>
 *     账号类型枚举
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/25 11:00 AM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum AccountTypeEnum {

    /**
     * 手机号
     **/
    PHONE(1),

    /**
     * 邮箱
     **/
    EMAIL(2),

    /**
     * 律师执业证号
     **/
    LAWYER_LICENSE(3),

    /**
     * 微信oauth
     **/
    WECHAT_OAUTH(4),


    ;

    private final int type;
}
