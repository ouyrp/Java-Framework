package com.wusong.uc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc DeviceTypeEnum
 * <p>
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/7 10:37 AM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum DeviceTypeEnum {

    ANDROID("ANDROID"), IOS("IOS"),

    ;

    private final String type;
}
