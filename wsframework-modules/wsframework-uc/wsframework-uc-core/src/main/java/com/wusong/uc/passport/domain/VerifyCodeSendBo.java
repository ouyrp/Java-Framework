package com.wusong.uc.passport.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import com.galaxy.ws.spec.common.core.param.Require;
import com.wusong.uc.account.domain.enums.AccountSystemEnum;
import com.wusong.uc.account.domain.enums.AccountTypeEnum;
import com.wusong.uc.passport.domain.enums.VerifyCodeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc VerifyCodeSendBo
 * <p>
 *     验证码发送bo
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 7:25 PM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class VerifyCodeSendBo {

    /**
     * 验证码类型
     **/
    @Require
    private VerifyCodeEnum type;

    /**
     * 发送对象账号
     **/
    @Require
    private String account;

    /**
     * 账号类型
     **/
    @Require
    private AccountTypeEnum accountType;

    /**
     * 账户系统code
     **/
    @Option
    private AccountSystemEnum accountSystem;
}
