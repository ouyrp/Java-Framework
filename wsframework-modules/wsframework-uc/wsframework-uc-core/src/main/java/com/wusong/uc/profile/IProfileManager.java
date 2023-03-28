package com.wusong.uc.profile;

import com.wusong.uc.common.IComponent;
import com.wusong.uc.common.annos.UcSdkFuture;
import com.wusong.uc.common.annos.UcSdkStable;
import com.wusong.uc.profile.domain.*;
import com.wusong.web.dto.ApiResult;

public interface IProfileManager extends IComponent {

    static IProfileManager instance(){
        return new ProfileManagerImpl();
    }


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
    ApiResult<ProfileBo> create(ProfileCreateBo bo);

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
    ApiResult<ProfileBo> copy(ProfileCopyBo bo);


    /**
     * javadoc find
     * @apiNote 查询
     *
     * @param bo 查询信息
     * @return com.wusong.web.dto.ApiResult<com.wusong.uc.profile.domain.ProfileBo>
     * @author weng xiaoyong
     * @date 2022/3/14 11:28 AM
     * @throws RuntimeException 任何IO异常均抛该异常
     **/
    @UcSdkStable
    ApiResult<ProfileBo> find(ProfileQueryBo bo);

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
    ApiResult<ProfileBo> update(ProfileUpdateBo bo);

}
