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
 * javadoc CustomVerifyCodeSendBo
 * <p>
 *     自定义验证码发送bo
 * <p>
 * @author weng xiaoyong
 * @date 2022/4/20 10:05
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CustomVerifyCodeSendBo {

    /**
     * 验证码类型
     **/
    @Require
    private VerifyCodeEnum type;

    /**
     * 发送对象账号
     **/
    @Require(notEmpty = true)
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

    /**
     * 短信发送app id
     **/
    @Require(notEmpty = true)
    private String appId;

    /**
     * 短息发送模板id
     **/
    @Require(notEmpty = true)
    private String templateId;

    /**
     * 短息发送内容, 必须预留一个 `%s` 占位符给验证码使用, 示例如下:
     * 您的无讼验证码是 %s , 请您准时参加考试
     **/
    @Require(notEmpty = true)
    private String content;

    /**
     * 有效期
     * 单位分钟
     * 必须 > 0
     **/
    @Require
    private Integer expire;
}
