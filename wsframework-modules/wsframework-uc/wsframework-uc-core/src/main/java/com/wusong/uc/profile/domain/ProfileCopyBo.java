package com.wusong.uc.profile.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import com.galaxy.ws.spec.common.core.param.Require;
import com.wusong.uc.profile.domain.enums.ProfileSystemEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc ProfileCopyBo
 * <p>
 *     拷贝创建账户名片
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/14 11:58 AM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ProfileCopyBo {

    /**
     * 账号id
     **/
    @Require
    private String accountId;

    /**
     * 创建到哪个系统
     **/
    @Require
    private ProfileSystemEnum profileSystem;

    /**
     * 从哪个系统拷贝
     * 如果不传默认UC
     **/
    @Option
    private ProfileSystemEnum fromProfileSystem;

}
