package com.wusong.uc.account.domain;

import com.galaxy.ws.spec.common.core.param.Require;
import com.wusong.uc.profile.domain.enums.ProfileSystemEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc AccountWithdrawBo
 * <p>
 *     账号注销信息bo
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/25 10:20 AM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AccountWithdrawBo {

    /**
     * 账号id
     * 业务使用该id流转
     **/
    @Require
    private String accountId;

    /**
     * 发起人所在的系统
     **/
    @Require
    private ProfileSystemEnum operatorUserSystem;

    /**
     * 操作人账号id
     **/
    @Require
    private String operatorAccountId;
}
