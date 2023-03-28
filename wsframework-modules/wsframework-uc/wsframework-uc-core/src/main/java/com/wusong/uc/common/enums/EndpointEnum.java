package com.wusong.uc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * javadoc EndpointEnum
 * <p>
 *     接入端枚举
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 7:16 PM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum EndpointEnum {

    /**
     * 正常web端
     **/
    WEB("PC"),

    /**
     * 小程序
     **/
    WX_APP("WX_APP"),

    /**
     * 移动端
     **/
    APP("APP"),

    /**
     * 桌面端应用
     **/
    H5("H5"),

    ;

    private final String endpoint;
}
