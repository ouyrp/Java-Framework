package com.wusong.uc.person.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum IdTypeEnum {

    /**
     * 二代身份证
     **/
    ID_CARD("ID_CARD"),

    ;

    private final String type;
}
