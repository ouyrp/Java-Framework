package com.wusong.uc.account.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import com.galaxy.ws.spec.common.core.param.Require;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc LoginAccountChangeBo
 * <p>
 *     修改登录账号
 * <p>
 * @author weng xiaoyong
 * @date 2022/4/6 15:04
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LoginAccountChangeBo {
    /**
     * 账号id
     **/
    @Require(notEmpty = true)
    private String accountId;

    /**
     * 原账号名称
     **/
    @Require(notEmpty = true)
    private String originAccountName;

    /**
     * 原账号类型
     **/
    @Require
    private Integer originAccountType;

    /**
     * 新账号名称
     **/
    @Require(notEmpty = true)
    private String newAccountName;

    /**
     * 新账号类型
     **/
    @Require
    private Integer newAccountType;

    /**
     * 密码
     **/
    @Option
    private byte[] password;

    /**
     * 验证码
     **/
    @Option
    private String verifyCode;

    /**
     * 操作人账号id
     **/
    @Require(notEmpty = true)
    private String operatorAccountId;
}
