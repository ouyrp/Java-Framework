package com.wusong.uc.account.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import com.galaxy.ws.spec.common.core.param.Require;
import com.wusong.uc.account.domain.enums.*;
import com.wusong.uc.common.enums.EndpointEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc AccountRegisterBo
 * <p>
 *     账户注册信息bo
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/25 4:13 PM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AccountRegisterBo {

    /**
     * 将新注册的账号挂载在该账号下
     **/
    @Option
    private String accountId;

    /**
     * 账号
     **/
    @Require
    private String account;

    /**
     * 账号类型
     **/
    @Require
    private AccountTypeEnum accountType;

    /**
     * 昵称
     **/
    @Option
    private String nickname;

    /**
     * 性别
     **/
    @Option
    private GenderEnum gender;

    /**
     * 头像url
     **/
    @Option
    private String photoUrl;

    /**
     * 所在省份code
     * 国标标准地区code
     **/
    @Option
    private String provinceCode;

    /**
     * 所在城市code
     * 国标标准地区code
     **/
    @Option
    private String cityCode;

    /**
     * 地址
     **/
    @Option
    private String address;

    /**
     * 手机号
     **/
    @Option
    private String phone;

    /**
     * 邮箱
     **/
    @Option
    private String email;

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
     * 端
     **/
    @Require
    private EndpointEnum endpoint;

    /**
     * 账号系统
     **/
    @Option
    private AccountSystemEnum accountSystem;
}
