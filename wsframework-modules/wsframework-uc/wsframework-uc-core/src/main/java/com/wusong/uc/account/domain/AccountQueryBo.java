package com.wusong.uc.account.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import com.wusong.uc.account.domain.enums.AccountTypeEnum;
import com.wusong.uc.account.domain.enums.AccountSystemEnum;
import com.wusong.uc.profile.domain.enums.ProfileSystemEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc AccountQueryBo
 * <p>
 *     账户查询方式, 本查询分两部分: 一部分是账号信息; 一部分是用户画像部分:
 *      1. accountId + profileSystem 查询 account
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/25 5:56 PM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AccountQueryBo {

    /**
     * 账号id
     **/
    @Option
    private String accountId;

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
     * 账户所属的系统
     * 默认外部用户
     **/
    @Option
    private AccountSystemEnum accountSystem;

    /**
     * 名片id
     **/
    @Option
    private String profileId;

    /**
     * profile 系统枚举
     **/
    @Option
    private ProfileSystemEnum profileSystem;
}
