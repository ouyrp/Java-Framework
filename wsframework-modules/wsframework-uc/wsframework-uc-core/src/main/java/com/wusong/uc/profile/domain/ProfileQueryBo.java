package com.wusong.uc.profile.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import com.wusong.uc.profile.domain.enums.ProfileSystemEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc ProfileQueryBo
 * <p>
 *
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/14 11:21 AM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ProfileQueryBo {

    /**
     * 账户id
     **/
    @Option
    private String accountId;

    /**
     * 名片id
     **/
    @Option
    private String profileId;

    /**
     * 名片所属的系统
     **/
    @Option
    private ProfileSystemEnum profileSystem;
}
