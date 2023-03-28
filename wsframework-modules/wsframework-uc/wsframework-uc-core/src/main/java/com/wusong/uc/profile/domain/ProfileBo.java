package com.wusong.uc.profile.domain;

import com.wusong.uc.account.domain.enums.GenderEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ProfileBo {

    /**
     * profile id
     **/
    private String profileId;

    /**
     * 账号id
     * 业务使用该id流转
     **/
    private String accountId;

    /**
     * 画像所属的系统
     **/
    private String systemCode;

    /**
     * 昵称
     **/
    private String nickname;

    /**
     * 性别
     * @see GenderEnum#getGender()
     **/
    private Integer gender;

    /**
     * 头像url
     **/
    private String photo;

    /**
     * 所在省份code
     * 国标标准地区code
     **/
    private String provinceCode;

    /**
     * 所在城市code
     * 国标标准地区code
     **/
    private String cityCode;

    /**
     * 地址
     **/
    private String address;

    /**
     * 手机号
     **/
    private String phone;

    /**
     * 邮箱
     **/
    private String email;

    /**
     * 记录创建时间
     * yyyy-MM-dd HH:mm:ss
     **/
    private String cts;

    /**
     * 记录更新时间
     * yyyy-MM-dd HH:mm:ss
     **/
    private String uts;
}
