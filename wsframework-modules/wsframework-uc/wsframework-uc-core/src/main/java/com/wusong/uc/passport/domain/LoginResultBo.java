package com.wusong.uc.passport.domain;

import com.wusong.uc.account.domain.AccountBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc LoginResultBo
 * <p>
 *     登录结果bo
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 11:11 AM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LoginResultBo {

    /**
     * 账号信息
     **/
    private AccountBo account;

    /**
     * token
     **/
    private String token;
}
