package com.wusong.uc.passport.domain;

import com.galaxy.ws.spec.common.core.param.Require;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc PasswordUpdateBo
 * <p>
 *     无校验密码更新bo
 *     注意 注意 注意:
 *     accountId 不能等于 operatorAccountId
 *     否则不给更新
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 2:07 PM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class NoAuthPasswordUpdateBo {
    /**
     * 目标账户id
     **/
    @Require(notEmpty = true)
    private String accountId;

    /**
     * 新密码
     **/
    @Require
    private byte[] password;

    /**
     * 操作人账号id
     **/
    @Require(notEmpty = true)
    private String operatorAccountId;
}
