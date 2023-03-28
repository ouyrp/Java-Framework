package com.wusong.uc.common.annos;


import java.lang.annotation.*;

/**
 * javadoc UcSdkFakeApi
 * <p>
 *     虚假的api(尚未实现)
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/1 11:32 AM
 * @version 1.0.0
 **/
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UcSdkFakeApi {
}
