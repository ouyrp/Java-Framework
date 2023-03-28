package com.wusong.uc.passport.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * javadoc SmsSendBo
 * <p>
 *     短信发送bo
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 2:23 PM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SmsSendBo {

    /**
     * 应用id
     **/
    private String appId;

    /**
     * 发送模板
     **/
    private String templateId;

    /**
     * 发送对象手机号列表
     **/
    private List<String> toUserPhones;

    /**
     * 参数列表
     **/
    private List<String> parameters;
}
