package com.wusong.uc.passport;

import com.galaxy.ws.spec.common.core.domain.RestResult;
import com.galaxy.ws.spec.common.core.param.Params;
import com.wusong.uc.account.domain.AccountBo;
import com.wusong.uc.account.mapper.AccountMapper;
import com.wusong.uc.common.SdkConfigs;
import com.wusong.uc.common.enums.SdkConfigKeyEnum;
import com.wusong.uc.common.module.profile.UcProfileApi;
import com.wusong.uc.passport.domain.*;
import com.wusong.uc.passport.mapper.PassportMapper;
import com.wusong.web.dto.ApiResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@SuppressWarnings(value = {"unused", "Duplicates", "unchecked"})
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PassportManagerImpl implements IPassportManager {

    @Override
    public ApiResult<LoginResultBo> login(LoginRequestBo bo) {
        if(!Params.check(bo, false)){
            bo.setPassword(new byte[0]);
            log.error("IPassportManager.login({}) 登录参数不合法, 无法登录", bo);
            return ApiResult.error("-1", "登录参数不合法, 无法登录");
        }
        final Map<String, Object> params = PassportMapper.loginBo2map(bo);
        try{
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).login(params).execute().body();
            if(Objects.isNull(result) || !result.successfully() || Objects.isNull(result.getData())){
                bo.setPassword(new byte[0]);
                params.put("password", new byte[0]);
                log.error("IPassportManager.login({}) invoke profileApi.login({}) 登录失败: [{}]", bo, params, result);
                return ApiResult.error("-1", (Objects.nonNull(result) ? result.getMessage() : "签证服务[PASSPORT]调用异常"));
            }
            final Map<String, Object> data = result.getData();
            final Map<String, Object> user = (Map<String, Object>) data.get("user");
            final String token = (String) data.get("token");
            return ApiResult.ok(new LoginResultBo().setToken(token).setAccount(AccountMapper.map2bo(user)));
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<LoginResultBo> businessLogin(BusinessLoginRequestBo bo) {
        if(!Params.check(bo, false)){
            bo.setPassword(new byte[0]);
            log.error("IPassportManager.businessLogin({}) 登录参数不合法, 无法登录", bo);
            return ApiResult.error("-1", "登录参数不合法, 无法登录");
        }
        final Map<String, Object> params = PassportMapper.businessLoginBo2map(bo);
        try{
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).businessLogin(params).execute().body();
            if(Objects.isNull(result) || !result.successfully() || Objects.isNull(result.getData())){
                bo.setPassword(new byte[0]);
                params.put("password", new byte[0]);
                log.error("IPassportManager.businessLogin({}) invoke profileApi.businessLogin({}) 登录失败: [{}]", bo, params, result);
                return ApiResult.error("-1", (Objects.nonNull(result) ? result.getMessage() : "签证服务[PASSPORT]调用异常"));
            }
            final Map<String, Object> data = result.getData();
            final Map<String, Object> user = (Map<String, Object>) data.get("user");
            final String token = (String) data.get("token");
            return ApiResult.ok(
                    new LoginResultBo()
                            .setToken(token)
                            .setAccount(AccountMapper.map2bo(user))
            );
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<TokenPayloadBo> parseToken(String token) {
        try {
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).parseToken(token).execute().body();
            if(Objects.isNull(result) || !result.successfully() || Objects.isNull(result.getData())){
                log.error("IPassportManager.parseToken({}) invoke profileApi.parseToken() 失败: [{}]", token, result);
                return ApiResult.error("-1", (Objects.nonNull(result) ? result.getMessage() : "签证服务[PASSPORT]调用异常"));
            }
            return ApiResult.ok(
                    PassportMapper.map2TokenBo(result.getData())
            );
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<TokenPayloadBo> expireToken(String token) {
        try {
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).expireToken(token).execute().body();
            if(Objects.isNull(result) || !result.successfully() || Objects.isNull(result.getData())){
                log.error("IPassportManager.expireToken({}) invoke profileApi.expireToken() 失败: [{}]", token, result);
                return ApiResult.error("-1", (Objects.nonNull(result) ? result.getMessage() : "签证服务[PASSPORT]调用异常"));
            }
            return ApiResult.ok(
                    PassportMapper.map2TokenBo(result.getData())
            );
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<AccountBo> updatePassword(PasswordUpdateBo bo) {
        if(!Params.check(bo, false)){
            bo.setPassword(new byte[0]).setNewPassword(new byte[0]);
            log.error("IPassportManager.updatePassword({}) 参数不合法, 无法修改密码", bo);
            return ApiResult.error("-1", "参数不合法, 无法修改密码");
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("accountId", bo.getAccountId());
        params.put("identifyingCode", bo.getVerifyCode());
        params.put("password", bo.getPassword());
        params.put("newPassword", bo.getNewPassword());
        try{
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).updatePassword(params).execute().body();
            if(Objects.isNull(result) || !result.successfully()){
                bo.setPassword(new byte[0]).setNewPassword(new byte[0]);
                log.error("IPassportManager.updatePassword({}) invoke profileApi.updatePassword() 修改密码失败", bo);
                return ApiResult.error("-1", (Objects.isNull(result) ? "签证服务[PASSPORT]调用失败" : result.getMessage()));
            }
            return ApiResult.ok(
                    AccountMapper.map2bo(result.getData())
            );
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<Integer> noAuthUpdatePassword(NoAuthPasswordUpdateBo bo) {
        if(!Params.check(bo, false)){
            bo.setPassword(new byte[0]);
            log.error("IPassportManager.noAuthUpdatePassword({}) 参数不合法, 无法修改密码", bo);
            return ApiResult.error("-1", "参数不合法, 无法修改密码");
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", bo.getAccountId());
        params.put("password", bo.getPassword());
        params.put("serviceName", SdkConfigs.loadConfig(SdkConfigKeyEnum.SERVICE_NAME));
        try{
            final RestResult<Map<String, Object>> result = Objects.requireNonNull(
                    SdkConfigs.api(UcProfileApi.class).noAuthUpdatePassword(params).execute().body()
            );
            if(!result.successfully()){
                bo.setPassword(new byte[0]);
                params.put("password", new byte[0]);
                log.error("IPassportManager.noAuthUpdatePassword({}) invoke UcProfileApi.noAuthUpdatePassword({}) 无认证修改密码失败: {}", bo, params, result);
                return ApiResult.error(String.valueOf(result.getCode()), result.getMessage());
            }
            if(Objects.nonNull(result.getData())){
                return ApiResult.ok(1);
            }else{
                return ApiResult.ok();
            }
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<Integer> sendVerifyCode(VerifyCodeSendBo bo) {
        if(!Params.check(bo, false)){
            log.error("IPassportManager.sendVerifyCode({}) 验证码发送参数不合法", bo);
            return ApiResult.error("-1", "验证码发送参数不合法");
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("type", bo.getType().getType());
        params.put("account", bo.getAccount());
        params.put("accountType", bo.getAccountType().getType());
        try{
            final RestResult<Integer> result = SdkConfigs.api(UcProfileApi.class).sendVerifyCode(params).execute().body();
            if(Objects.isNull(result) || !result.successfully()){
                log.error("IPassportManager.sendIdentifyingCode({}) invoke profileApi.sendIdentifyingCode({}) 验证码发送失败: [{}]", bo, params, result);
                return ApiResult.error("-1", (Objects.isNull(result) ? "签证服务[PASSPORT]": result.getMessage()));
            }
            return ApiResult.ok(result.getData());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<Integer> sendCustomVerifyCode(CustomVerifyCodeSendBo bo) {
        if(!Params.check(bo, false)){
            log.error("IPassportManager.sendCustomVerifyCode({}) 验证码发送参数不合法", bo);
            return ApiResult.error("-1", "验证码发送参数不合法");
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("type", bo.getType().getType());
        params.put("account", bo.getAccount());
        params.put("accountType", bo.getAccountType().getType());
        params.put("appId", bo.getAppId());
        params.put("templateId", bo.getTemplateId());
        params.put("content", bo.getContent());
        params.put("expire", bo.getExpire());
        try{
            final RestResult<Integer> result = SdkConfigs.api(UcProfileApi.class).sendCustomIdentifyingCode(params).execute().body();
            if(Objects.isNull(result) || !result.successfully()){
                log.error("IPassportManager.sendCustomVerifyCode({}) invoke profileApi.sendCustomIdentifyingCode({}) 验证码发送失败: [{}]", bo, params, result);
                return ApiResult.error("-1", (Objects.isNull(result) ? "签证服务[PASSPORT]": result.getMessage()));
            }
            return ApiResult.ok(result.getData());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
