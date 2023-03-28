package com.wusong.uc.person.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc AuthorizationTypeEnum
 * <p>
 * 认证类型枚举
 * <p>
 *
 * @author weng xiaoyong
 * @version 1.0.0
 * @date 2022/2/28 2:41 PM
 **/
@AllArgsConstructor
@Getter
@ToString
public enum AuthorizationTypeEnum {
    /**
     * 二要素: 身份证号 与姓名验证
     **/
    TWO_FACTOR_ID_NAME("ID_NAME"),

    /**
     * 人脸活体
     **/
    FACE("FACE"),
    ;

    private final String type;
}
