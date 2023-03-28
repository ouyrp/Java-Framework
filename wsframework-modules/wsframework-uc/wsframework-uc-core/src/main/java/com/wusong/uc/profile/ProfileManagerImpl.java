package com.wusong.uc.profile;

import com.galaxy.ws.spec.common.core.domain.RestResult;
import com.galaxy.ws.spec.common.core.param.Params;
import com.wusong.uc.common.SdkConfigs;
import com.wusong.uc.common.module.profile.UcProfileApi;
import com.wusong.uc.profile.domain.*;
import com.wusong.uc.profile.mapper.ProfileMapper;
import com.wusong.web.dto.ApiResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.shared.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * javadoc UserManagerImpl
 * <p>
 *     用户管理服务实现
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/4 2:31 PM
 * @version 1.0.0
 **/
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class ProfileManagerImpl implements IProfileManager {


    @Override
    public ApiResult<ProfileBo> create(ProfileCreateBo bo) {
        return ApiResult.error("-1", "暂不支持的功能");
    }

    @Override
    public ApiResult<ProfileBo> copy(ProfileCopyBo bo) {
        return ApiResult.error("-1", "暂不支持的功能");
    }

    @Override
    public ApiResult<ProfileBo> find(ProfileQueryBo bo) {
        final Map<String, Object> params = new HashMap<>();
        if(StringUtils.isNotEmpty(bo.getAccountId())){
            params.put("userId", bo.getAccountId());
        }else{
            if(StringUtils.isNotEmpty(bo.getProfileId())){
                params.put("userId", bo.getProfileId());
            }
        }
        if(Objects.nonNull(bo.getProfileSystem())){
            params.put("systemCode", bo.getProfileSystem().getSystem());
        }
        try{
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).find(params).execute().body();
            if(Objects.isNull(result) || !result.successfully()){
                log.error("IProfileManager.find({}) invoke profileApi.find({}) 查询失败: [{}]", bo, params, result);
                return ApiResult.error("-1", (Objects.isNull(result) ? "名片服务调用异常" : result.getMessage()));
            }
            if(Objects.isNull(result.getData())){
                return ApiResult.ok();
            }
            return ApiResult.ok(ProfileMapper.buildUserBo(result.getData()));
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ApiResult<ProfileBo> update(ProfileUpdateBo bo) {
        if(!Params.check(bo, false)){
            log.error("IProfileManager.update({}) 名片更新参数不合法, 无法更新", bo);
            return ApiResult.error("-1", "名片更新参数不合法");
        }
        final Map<String, Object> params = ProfileMapper.buildUpdateParams(bo);
        try{
            final RestResult<Map<String, Object>> result = SdkConfigs.api(UcProfileApi.class).updateUser(params).execute().body();
            if(Objects.isNull(result) || !result.successfully()){
                log.error("IProfileManager.update({}) invoke profileApi.updateUser({}) 更新用户失败: [{}]", bo, params, result);
                return ApiResult.error("-1", (Objects.isNull(result) ? "名片服务调用异常": result.getMessage()));
            }
            if(Objects.isNull(result.getData())){
                return ApiResult.ok();
            }
            return ApiResult.ok(ProfileMapper.buildUserBo(result.getData()));
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
