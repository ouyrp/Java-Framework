package com.wusong.uc.account;

import com.wusong.uc.account.domain.*;
import com.wusong.uc.common.IComponent;
import com.wusong.uc.common.annos.UcSdkFuture;
import com.wusong.uc.common.annos.UcSdkStable;
import com.wusong.uc.common.annos.UcSdkTemporary;
import com.wusong.web.dto.ApiResult;

@SuppressWarnings(value = "unused")
public interface IAccountManager extends IComponent {


    static IAccountManager instance(){
        return new AccountManagerImpl();
    }

    /**
     * javadoc find
     * @apiNote 查询账号信息
     *
     * @param bo 查询信息
     * @return com.wusong.uc.account.domain.AccountBo
     * @author weng xiaoyong
     * @date 2022/2/28 9:56 AM
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    ApiResult<AccountBo> find(AccountQueryBo bo);

    /**
     * javadoc register
     * @apiNote 账号注册
     *          最简的情况下只需要传3个参数: account(+ accountType), verifyCode(验证码), EndpointEnum(注册端来源)
     *
     * @param bo 注册信息
     * @return com.wusong.uc.account.domain.AccountBo
     * @author weng xiaoyong
     * @date 2022/2/28 12:13 PM
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkFuture
    ApiResult<AccountBo> register(AccountRegisterBo bo);

    /**
     * javadoc withdraw
     * @apiNote 账号注销
     *
     * @param bo 注销信息
     * @return com.wusong.uc.account.domain.AccountBo
     * @author weng xiaoyong
     * @date 2022/3/4 5:00 PM
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkFuture
    ApiResult<AccountBo> withdraw(AccountWithdrawBo bo);

    /**
     * javadoc bindWx
     * @apiNote 账号绑定wx
     *
     * @param accountId 账号id
     * @param unionId 微信平台union id
     * @return com.wusong.web.dto.ApiResult<com.wusong.uc.account.domain.AccountBo>
     * @author weng xiaoyong
     * @date 2022/3/25 10:49
     **/
    @UcSdkTemporary
    ApiResult<AccountBo> bindWx(String accountId, String unionId);

    /**
     * javadoc unBindWx
     * @apiNote 解除账号绑定wx
     *
     * @param accountId 账号id
     * @return com.wusong.web.dto.ApiResult<com.wusong.uc.account.domain.AccountBo>
     * @author weng xiaoyong
     * @date 2022/3/25 10:49
     **/
    @UcSdkTemporary
    ApiResult<AccountBo> unBindWx(String accountId);

    /**
     * javadoc changeLoginAccountName
     * @apiNote 修改登录账号
     *
     * @param bo 登录账号修改信息bo
     * @return com.wusong.web.dto.ApiResult<com.wusong.uc.account.domain.AccountBo>
     * @author weng xiaoyong
     * @date 2022/4/6 10:12
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkTemporary
    ApiResult<AccountBo> changeLoginAccountName(LoginAccountChangeBo bo);
}
