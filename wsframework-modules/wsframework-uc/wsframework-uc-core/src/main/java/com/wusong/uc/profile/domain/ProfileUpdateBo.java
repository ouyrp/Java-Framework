package com.wusong.uc.profile.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import com.galaxy.ws.spec.common.core.param.Require;
import com.wusong.uc.account.domain.enums.GenderEnum;
import com.wusong.uc.profile.domain.enums.ProfileSystemEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc UserUpdateBo
 * <p>
 *     用户更新
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/4 2:44 PM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ProfileUpdateBo {

    /**
     * 用户id
     **/
    @Require
    private String profileId;

    /**
     * 用户所属的系统
     **/
    @Require
    private ProfileSystemEnum userSystem;

    /**
     * 昵称
     **/
    @Option
    private String nickname;

    /**
     * 性别
     * @see GenderEnum#getGender()
     **/
    @Option
    private GenderEnum gender;

    /**
     * 头像url
     **/
    @Option
    private String photo;

    /**
     * 所在省份code
     * 国标标准地区code
     **/
    @Option
    private String provinceCode;

    /**
     * 所在城市code
     * 国标标准地区code
     **/
    @Option
    private String cityCode;

    /**
     * 地址
     **/
    @Option
    private String address;

    /**
     * 手机号
     **/
    @Option
    private String phone;

    /**
     * 邮箱
     **/
    @Option
    private String email;
}
