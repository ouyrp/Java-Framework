package com.wusong.uc.account.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc AccountStatusEnum
 * <p>
 *     账号状态枚举
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/25 11:02 AM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum AccountStatusEnum {

    /**
     * 正常
     **/
    NORMAL(1),

    /**
     * 限制
     **/
    LIMITED(2),

    /**
     * 禁用
     **/
    DISABLE(3),

    /**
     * 注销
     **/
    WITHDRAW(4),

    ;

    private final int status;
}
