package com.wusong.uc.account;

import com.galaxy.ws.spec.common.core.domain.RestResult;
import com.galaxy.ws.spec.common.core.param.Params;
import com.wusong.uc.account.domain.*;
import com.wusong.uc.account.domain.enums.AccountSystemEnum;
import com.wusong.uc.account.domain.enums.AccountTypeEnum;
import com.wusong.uc.account.mapper.AccountMapper;
import com.wusong.uc.common.SdkConfigs;
import com.wusong.uc.common.enums.SdkConfigKeyEnum;
import com.wusong.uc.common.module.profile.UcProfileApi;
import com.wusong.web.dto.ApiResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * javadoc AccountManagerImpl
 * <p>
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/25 5:15 PM
 * @version 1.0.0
 **/
@SuppressWarnings(value = "unused")
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountManagerImpl implements IAccountManager {

    @Override
    public ApiResult<AccountBo> find(AccountQueryBo bo) {
        if(!Params.check(bo, false)){
            log.error("IAccountManager.find({}) 账户查询参数不合法", bo);
            return ApiResult.error("-1", "账户查询参数不合法");
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", bo.getAccountId());
        if(Objects.equals(AccountTypeEnum.PHONE, bo.getAccountType())){
            params.put("phone", bo.getAccount());
        }else if(Objects.equals(AccountTypeEnum.EMAIL, bo.getAccountType())){
            params.put("email", bo.getAccount());
        }else if(Objects.equals(AccountTypeEnum.WECHAT_OAUTH, bo.getAccountType())){
            params.put("wxUnionId", bo.getAccount());
        }
        if(Objects.nonNull(bo.getAccountSystem())){
            params.put("accountSystemCode", bo.getAccountSystem().getCode());
        }else{
            params.put("accountSystemCode", AccountSystemEnum.EXTERNAL.getCode());
        }
        if(Objects.nonNull(bo.getProfileId())){
            params.put("profileId", bo.getProfileId());
        }
        if(Objects.nonNull(bo.getProfileSystem())){
            params.put("profileSystemCode", bo.getProfileSystem().getSystem());
        }

        try{
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).find(params).execute().body();
            if(Objects.isNull(result) || !result.successfully()){
                log.error("IAccountManager.find({}) invoke profileApi.find({}) resp is illegal: [{}]", bo, params, result);
                return ApiResult.error("-1", Objects.isNull(result) ? "账户服务调用失败" : result.getMessage());
            }
            if(Objects.nonNull(result.getData())){
                return ApiResult.ok(AccountMapper.map2bo(result.getData()));
            }
            return ApiResult.ok();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<AccountBo> register(AccountRegisterBo bo) {
        if(!Params.check(bo, false)){
            log.error("IUserManager.register({}) 账户注册信息不合法, 无法注册", bo);
            return ApiResult.error("-1", "账户注册信息不合法, 无法注册");
        }
        final Map<String, Object> params = AccountMapper.buildRegisterParams(bo);
        try {
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).createUser(params).execute().body();
            if(Objects.isNull(result) || !result.successfully() || Objects.isNull(result.getData())){
                bo.setPassword(new byte[0]);
                log.error("IAccountManager.register({}) invoke profileApi.createUser({}) 注册失败: [{}]", bo, params, result);
                return ApiResult.error("-1", (Objects.isNull(result) ? "账户服务调用异常" : result.getMessage()));
            }
            return ApiResult.ok(
                    AccountMapper.map2bo(result.getData())
            );
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<AccountBo> withdraw(AccountWithdrawBo bo) {
        if(!Params.check(bo, false)){
            log.error("IAccountManager.withdraw({}) 注销参数不合法", bo);
            return ApiResult.error("-1", "账户注销参数不合法, 无法注销");
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", bo.getAccountId());
        params.put("operatorId", bo.getOperatorAccountId());
        try{
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).delete(params).execute().body();
            if(Objects.isNull(result) || !result.successfully()){
                log.error("IAccountManager.withdraw({}) invoke profileApi.delete({}) 注册失败: [{}]", bo, params, result);
                return ApiResult.error("-1", (Objects.isNull(result) ? "账户服务调用异常": result.getMessage()));
            }
            if(Objects.isNull(result.getData())){
                return ApiResult.ok();
            }
            return ApiResult.ok(AccountMapper.map2bo(result.getData()));
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<AccountBo> bindWx(String accountId, String unionId) {
        if(Objects.isNull(accountId) || accountId.isEmpty() || Objects.isNull(unionId) || unionId.isEmpty()){
            log.error("IAccountManager.bindWx({}, {}) 绑定微信参数不合法, 无法绑定", accountId, unionId);
            return ApiResult.error("-1", "绑定微信参数不合法, 无法绑定");
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", accountId);
        params.put("wxUnionId", unionId);
        try{
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).bindWx(params).execute().body();
            if(Objects.isNull(result) || !result.successfully()){
                log.error("IAccountManager.bindWx() invoke profileApi.bindWx({}) 绑定微信失败: [{}]", params, result);
                return ApiResult.error("-1", (Objects.isNull(result) ? "账户服务调用异常": result.getMessage()));
            }
            if(Objects.isNull(result.getData())){
                return ApiResult.ok();
            }
            return ApiResult.ok(AccountMapper.map2bo(result.getData()));
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<AccountBo> unBindWx(String accountId) {
        if(Objects.isNull(accountId) || accountId.isEmpty()){
            log.error("IAccountManager.unBindWx({}) 解除绑定微信参数不合法, 无法解除绑定", accountId);
            return ApiResult.error("-1", "解除绑定微信参数不合法, 无法解除绑定");
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", accountId);
        try{
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).unBindWx(params).execute().body();
            if(Objects.isNull(result) || !result.successfully()){
                log.error("IAccountManager.unBindWx() invoke profileApi.unBindWx({}) 解除绑定微信失败: [{}]", params, result);
                return ApiResult.error("-1", (Objects.isNull(result) ? "账户服务调用异常": result.getMessage()));
            }
            if(Objects.isNull(result.getData())){
                return ApiResult.ok();
            }
            return ApiResult.ok(AccountMapper.map2bo(result.getData()));
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<AccountBo> changeLoginAccountName(LoginAccountChangeBo bo) {
        if(!Params.check(bo, false)){
            log.error("IAccountManager.changeLoginAccountName({}) 修改登录账号参数错误, 无法修改", bo);
            return ApiResult.error("-1", "修改登录账号参数错误, 无法修改");
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", bo.getOperatorAccountId());
        params.put("phone", bo.getNewAccountName());
        params.put("serviceName", SdkConfigs.loadConfig(SdkConfigKeyEnum.SERVICE_NAME));
        try{
            final RestResult<Map<String, Object>> result = Objects.requireNonNull(
                    SdkConfigs.api(UcProfileApi.class).bindingPhone(params).execute().body()
            );
            if(!result.successfully()){
                log.error("IAccountManager.changeLoginAccountName() invoke profileApi.bindingPhone({}) 绑定手机号失败: [{}]", params, result);
                return ApiResult.error(String.valueOf(result.getCode()), result.getMessage());
            }
            if(Objects.isNull(result.getData())){
                return ApiResult.ok();
            }
            return ApiResult.ok(AccountMapper.map2bo(result.getData()));
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
