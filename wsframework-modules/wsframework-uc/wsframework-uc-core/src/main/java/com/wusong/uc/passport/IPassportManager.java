package com.wusong.uc.passport;

import com.wusong.uc.account.domain.AccountBo;
import com.wusong.uc.common.IComponent;
import com.wusong.uc.common.annos.UcSdkStable;
import com.wusong.uc.passport.domain.*;
import com.wusong.web.dto.ApiResult;

@SuppressWarnings(value = "unused")
public interface IPassportManager extends IComponent {

    static IPassportManager instance(){
        return new PassportManagerImpl();
    }

    /**
     * javadoc login
     * @apiNote 普通登录
     *
     * @param bo 登录信息
     * @return com.wusong.uc.passport.domain.LoginResultBo
     * @author weng xiaoyong
     * @date 2022/2/28 11:17 AM
     * @throws com.wusong.uc.passport.domain.exception.PassportException 任何逻辑不正确均抛出该异常
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    ApiResult<LoginResultBo> login(LoginRequestBo bo);


    /**
     * javadoc businessLogin
     * @apiNote B 端登录
     *
     * @param bo 登录信息
     * @return com.wusong.uc.passport.domain.LoginRequestBo
     * @author weng xiaoyong
     * @date 2022/3/2 10:59 AM
     * @throws com.wusong.uc.passport.domain.exception.PassportException 任何逻辑不正确均抛出该异常
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    ApiResult<LoginResultBo> businessLogin(BusinessLoginRequestBo bo);

    /**
     * javadoc parseToken
     * @apiNote 解析令牌信息
     *
     * @param token jwt令牌
     * @return com.wusong.uc.passport.domain.TokenPayloadBo
     * @author weng xiaoyong
     * @date 2022/2/28 11:21 AM
     * @throws com.wusong.uc.passport.domain.exception.PassportException 任何逻辑不正确均抛出该异常
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    ApiResult<TokenPayloadBo> parseToken(String token);

    /**
     * javadoc expireToken
     * @apiNote 让token失效
     *
     * @param token 令牌信息
     * @return com.wusong.uc.passport.domain.TokenPayloadBo
     * @author weng xiaoyong
     * @date 2022/2/28 11:53 AM
     * @throws com.wusong.uc.passport.domain.exception.PassportException 任何逻辑不正确均抛出该异常
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    ApiResult<TokenPayloadBo> expireToken(String token);

    /**
     * javadoc updatePassword
     * @apiNote 更新密码
     *
     * @param bo 密码更新信息
     * @return com.wusong.uc.account.domain.AccountBo
     * @author weng xiaoyong
     * @date 2022/2/28 2:10 PM
     * @throws com.wusong.uc.passport.domain.exception.PassportException 任何逻辑不正确均抛出该异常
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    ApiResult<AccountBo> updatePassword(PasswordUpdateBo bo);

    /**
     * javadoc noAuthUpdatePassword
     * @apiNote 无认证更新密码
     *          该接口由严格审计 严格审计 严格审计
     *
     * @param bo 更新参数 目标accountId 不能等于 操作人accountId(也就是说账户不能自我无认证更新), 否则不给更新;
     * @return com.wusong.web.dto.ApiResult<java.lang.Integer>
     * @author weng xiaoyong
     * @date 2022/4/2 15:08
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    ApiResult<Integer> noAuthUpdatePassword(NoAuthPasswordUpdateBo bo);


    /**
     * javadoc sendVerifyCode
     * @apiNote 发送验证码
     *
     * @param bo 请求信息
     * @return int > 0成功
     * @author weng xiaoyong
     * @date 2022/2/28 7:28 PM
     * @throws com.wusong.uc.passport.domain.exception.PassportException 任何逻辑不正确均抛出该异常
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    ApiResult<Integer> sendVerifyCode(VerifyCodeSendBo bo);

    /**
     * javadoc sendCustomVerifyCode
     * @apiNote 发送定制的验证码信息
     *
     * @param bo 定制验证码信息
     * @return com.wusong.web.dto.ApiResult<java.lang.Integer>
     * @author weng xiaoyong
     * @date 2022/4/20 10:12
     **/
    @UcSdkStable
    ApiResult<Integer> sendCustomVerifyCode(CustomVerifyCodeSendBo bo);
}
