package com.wusong.uc.account;

import com.wusong.uc.account.domain.*;
import com.wusong.uc.common.SdkConfigs;
import com.wusong.uc.common.annos.UcSdkStable;
import com.wusong.uc.common.annos.UcSdkTemporary;
import com.wusong.web.dto.ApiResult;
import lombok.*;

@SuppressWarnings(value = "unused")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Accounts {



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
    public static ApiResult<AccountBo> find(AccountQueryBo bo){
        return SdkConfigs.component(IAccountManager.class).find(bo);
    }

    /**
     * javadoc register
     * @apiNote 账号注册
     *
     * @param bo 注册信息
     * @return com.wusong.uc.account.domain.AccountBo
     * @author weng xiaoyong
     * @date 2022/2/28 12:13 PM
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    public static ApiResult<AccountBo> register(AccountRegisterBo bo){
        return SdkConfigs.component(IAccountManager.class).register(bo);
    }

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
    public static ApiResult<AccountBo> bindWx(String accountId, String unionId){
        return SdkConfigs.component(IAccountManager.class).bindWx(accountId, unionId);
    }

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
    public static ApiResult<AccountBo> unBindWx(String accountId){
        return SdkConfigs.component(IAccountManager.class).unBindWx(accountId);
    }

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
    public static ApiResult<AccountBo> changeLoginAccountName(LoginAccountChangeBo bo){
        return SdkConfigs.component(IAccountManager.class).changeLoginAccountName(bo);
    }
}
