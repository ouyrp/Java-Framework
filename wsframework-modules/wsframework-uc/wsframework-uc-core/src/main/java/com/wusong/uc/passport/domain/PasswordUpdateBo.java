package com.wusong.uc.passport.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import com.galaxy.ws.spec.common.core.param.Require;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc PasswordUpdateBo
 * <p>
 *     密码更新bo
 *     如果使用验证码更新, 则 verifyCode 和 password是必须的
 *     如果使用旧密码更新, 则 password 和 newPassword是必须的
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 2:07 PM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PasswordUpdateBo {

    /**
     * 账号id
     **/
    @Require
    private String accountId;

    /**
     * 验证码
     **/
    @Option
    private String verifyCode;

    /**
     * 旧密码
     **/
    @Option
    private byte[] password;

    /**
     * 新密码
     **/
    @Require
    private byte[] newPassword;
}
