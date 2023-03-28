package com.wusong.uc.profile;

import com.wusong.uc.common.SdkConfigs;
import com.wusong.uc.common.annos.UcSdkFuture;
import com.wusong.uc.common.annos.UcSdkStable;
import com.wusong.uc.profile.domain.*;
import com.wusong.web.dto.ApiResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * javadoc Users
 * <p>
 *     用户相关静态方法
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/4 3:09 PM
 * @version 1.0.0
 **/
@SuppressWarnings(value = "unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Profiles {

    /**
     * javadoc create
     * @apiNote 创建账户名片
     *
     * @param bo 创建信息
     * @return com.wusong.web.dto.ApiResult<com.wusong.uc.profile.domain.ProfileBo>
     * @author weng xiaoyong
     * @date 2022/3/14 11:57 AM
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkFuture
    public static ApiResult<ProfileBo> create(ProfileCreateBo bo){
        return SdkConfigs.component(IProfileManager.class).create(bo);
    }


    /**
     * javadoc copy
     * @apiNote 拷贝创建名片
     *
     * @param bo 拷贝信息
     * @return com.wusong.web.dto.ApiResult<com.wusong.uc.profile.domain.ProfileBo>
     * @author weng xiaoyong
     * @date 2022/3/14 11:59 AM
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkFuture
    public static ApiResult<ProfileBo> copy(ProfileCopyBo bo){
        return SdkConfigs.component(IProfileManager.class).copy(bo);
    }

    /**
     * javadoc find
     * @apiNote 用户画像查询
     *
     * @param bo 用户画像查询信息
     * @return com.wusong.uc.user.domain.UserBo
     * @author weng xiaoyong
     * @date 2022/3/4 2:33 PM
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    public static ApiResult<ProfileBo> find(ProfileQueryBo bo){
        return SdkConfigs.component(IProfileManager.class).find(bo);
    }

    /**
     * javadoc update
     * @apiNote 更新用户
     *
     * @param bo 更新信息
     * @return com.wusong.uc.user.domain.UserBo
     * @author weng xiaoyong
     * @date 2022/3/4 2:46 PM
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    public static ApiResult<ProfileBo> update(ProfileUpdateBo bo){
        return SdkConfigs.component(IProfileManager.class).update(bo);
    }
}
