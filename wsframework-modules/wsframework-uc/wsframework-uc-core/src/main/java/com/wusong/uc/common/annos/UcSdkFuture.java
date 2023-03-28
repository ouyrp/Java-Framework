package com.wusong.uc.common.annos;

import java.lang.annotation.*;

/**
 * javadoc SdkFuture
 * <p>
 *     代表着未来会实现
 *     在本个窗口期内未实现
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/1 11:31 AM
 * @version 1.0.0
 **/
@Target(value = {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UcSdkFuture {
}
