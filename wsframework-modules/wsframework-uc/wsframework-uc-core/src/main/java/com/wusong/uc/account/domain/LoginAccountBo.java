package com.wusong.uc.account.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc LoginAccountBo
 * <p>
 *     可登录的账号
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/15 2:06 PM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LoginAccountBo {

    /**
     * 账号名称
     **/
    private String accountName;

    /**
     * 账号类型
     **/
    private Integer accountType;

    /**
     * 账号状态
     **/
    private Integer accountStatus;
}
