package com.wusong.uc.account.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * javadoc GenderEnum
 * <p>
 *     性别枚举
 * <p>
 * @author weng xiaoyong
 * @date 2021/8/3 6:52 PM
 * @version 1.0.0
 **/
@AllArgsConstructor
@Getter
@ToString
public enum GenderEnum {

    /**
     * 未知
     **/
    UNKNOWN(0),

    /**
     * 男性
     **/
    MALE(1),

    /**
     * 女性
     **/
    FEMALE(2),
    ;

    private final int gender;

    public static boolean legal(Integer gender){
        if(Objects.isNull(gender)){
            return false;
        }
        for(GenderEnum ge : values()){
            if(ge.gender == gender){
                return true;
            }
        }
        return false;
    }

    public static int buildGender(Integer gender){
        if(legal(gender)){
            return gender;
        }
        return GenderEnum.UNKNOWN.getGender();
    }
}
