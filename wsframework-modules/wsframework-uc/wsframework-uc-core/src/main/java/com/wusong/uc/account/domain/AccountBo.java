package com.wusong.uc.account.domain;

import com.wusong.uc.account.domain.enums.AccountProductLineEnum;
import com.wusong.uc.account.domain.enums.AccountStatusEnum;
import com.wusong.uc.common.enums.EndpointEnum;
import com.wusong.uc.profile.domain.ProfileBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * javadoc AccountBo
 * <p>
 *     账号信息bo
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/25 10:20 AM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AccountBo {

    /**
     * 账号id
     **/
    private String accountId;

    /**
     * 可登录账号列表
     **/
    private List<LoginAccountBo> accounts;

    /**
     * 账号所属自然人id
     **/
    private String personId;

    /**
     * 账号系统码
     **/
    private String accountSystemCode;

    /**
     * 账号所属产品线
     * @see AccountProductLineEnum#getLine()
     **/
    private String productLine;

    /**
     * 账号状态
     * @see AccountStatusEnum#getStatus()
     **/
    private Integer status;

    /**
     * 账号注册端
     * @see EndpointEnum#getEndpoint()
     **/
    private String registerEndpoint;

    /**
     * 账号注册时间
     **/
    private String registerTime;

    /**
     * last login timestamp
     * 最后登录时间
     * yyyy-MM-dd HH:mm:ss
     **/
    private String lastLoginTime;

    /**
     * 名片信息
     **/
    private ProfileBo profile;


}
