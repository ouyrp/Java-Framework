package com.wusong.uc.passport.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import com.galaxy.ws.spec.common.core.param.Require;
import com.wusong.uc.account.domain.enums.AccountTypeEnum;
import com.wusong.uc.account.domain.enums.AccountSystemEnum;
import com.wusong.uc.common.enums.DeviceTypeEnum;
import com.wusong.uc.common.enums.EndpointEnum;
import com.wusong.uc.profile.domain.enums.ProfileSystemEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc LoginBo
 * <p>
 *     登录请求信息bo
 *     如果使用账号登录 则 account + accountType是必须的
 *     如果使用account id登录则只传入account id即可
 *     如果使用验证码登录 则不必传入password
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 11:02 AM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LoginRequestBo {

    /**
     * 账号
     **/
    @Option
    private String account;

    /**
     * 账号类型
     **/
    @Option
    private AccountTypeEnum accountType;

    /**
     * 系统码
     **/
    @Require
    private AccountSystemEnum systemCode;

    /**
     * 验证码
     **/
    @Option
    private String verifyCode;

    /**
     * 密码
     **/
    @Option
    private byte[] password;

    /**
     * 端来源
     **/
    @Require
    private EndpointEnum endpoint;

    /**
     * 登录客户端类型
     * 如果是从app登录则必须传入
     **/
    @Option
    private DeviceTypeEnum deviceType;

    /**
     * 登录设备id
     * (utf8) 64字符以内
     **/
    @Option
    private String deviceId;

    /**
     * 超时时间, 单位秒
     * 如果不默认超时时间 30天
     **/
    @Require
    private Long expire;

    /**
     * 账户画像所属的系统名称
     **/
    @Option
    private ProfileSystemEnum profileSystem;
}
