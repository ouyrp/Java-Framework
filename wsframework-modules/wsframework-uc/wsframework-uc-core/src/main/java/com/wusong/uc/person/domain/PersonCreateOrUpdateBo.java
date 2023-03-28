package com.wusong.uc.person.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import com.galaxy.ws.spec.common.core.param.Require;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * javadoc PersonCreateOrUpdateBo
 * <p>
 *     自然人认证数据创建/更新bo
 * <p>
 * @author weng xiaoyong
 * @date 2022/4/14 16:11
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PersonCreateOrUpdateBo {

    /**
     * 自然人名称
     **/
    @Require(notEmpty = true)
    private String name;

    /**
     * 自然人身份证件号
     **/
    @Option
    private String idCardNo;

    /**
     * 自然人身份证件类型
     **/
    @Option
    private String idCardType;

    /**
     * 身份证件有效期开始时间
     * yyyy-MM-dd HH:mm:ss
     **/
    @Option
    private String idCardExpirationStart;

    /**
     * 身份证件有效期结束时间
     * yyyy-MM-dd HH:mm:ss
     **/
    @Option
    private String idCardExpirationEnd;

    /**
     * 手机号
     **/
    @Option
    private String phone;

    /**
     * 认证类型
     **/
    @Require(notEmpty = true)
    private List<String> authTypes;

    /**
     * 关联的账户id
     **/
    @Require(notEmpty = true)
    private String accountId;

    /**
     * 认证检测任务id
     **/
    @Require(notEmpty = true)
    private String authTaskId;
}
