package com.wusong.uc.common.annos;

import java.lang.annotation.*;

/**
 * javadoc UcSdkTemporary
 * <p>
 *     临时性的sdk接口, 随时会取消
 *     但只要存在, 就可用
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/25 10:40
 * @version 1.0.0
 **/
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UcSdkTemporary {
}
