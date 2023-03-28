package com.wusong.uc.common.annos;


import java.lang.annotation.*;

/**
 * javadoc SdkAvailable
 * <p>
 *     代表稳定可用
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/1 11:32 AM
 * @version 1.0.0
 **/
@Target(value = {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UcSdkStable {
}
