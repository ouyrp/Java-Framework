package com.wusong.uc.passport.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc TokenPayloadBo
 * <p>
 *     令牌有效荷载
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 11:18 AM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class TokenPayloadBo {

    /**
     * 令牌id
     **/
    private String tokenId;

    /**
     * 登录account id
     **/
    private String accountId;

    /**
     * 账号名
     **/
    private String accountName;

    /**
     * 账号类型
     **/
    private Integer accountType;

    /**
     * 名片id
     **/
    private String profileId;

    /**
     * 系统code
     **/
    private String systemCode;

    /**
     * 登录设备id
     **/
    private String deviceId;

    /**
     * 商户id
     **/
    private String merchantId;

    /**
     * 额外参数
     **/
    private String extraParam;

    /**
     * 生成时间
     * yyyy-MNM-dd HH:mm:ss
     **/
    private String cts;

    /**
     * 更新时间
     * yyyy-MNM-dd HH:mm:ss
     **/
    private String uts;

    /**
     * 过期时间
     * yyyy-MNM-dd HH:mm:ss
     **/
    private String ets;

    /**
     * 是否还可用
     * > 0 可用
     **/
    private Integer available;
}
