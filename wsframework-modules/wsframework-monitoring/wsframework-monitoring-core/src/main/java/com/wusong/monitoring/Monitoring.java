package com.wusong.monitoring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author p14
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitoring {

    /**
     * 用于日志、指标tag等。为空时，默认取类名
     * @return
     */
    String value() default "";

    /**
     * 要打印的方法参数，支持el表达式（注意脱敏）。只有注解在方法上时，该参数才生效，注解在类上不生效。例子： #user.toShadowString()
     * @return
     */
    String logMethodParameter() default "";

    /**
     * 打印方法的返回值，支持el表达式，其中 #return 代表返回值（注意脱敏）。只有注解在方法上时，该参数才生效，注解在类上不生效。例子： #return.shadowName
     * 如果涉及脱敏且要打印所有返回值，那么直接用 #return 即可
     * @return
     */
    String logReturnedValue() default "";

    /**
     * 打印所有参数和返回值。！！设置为true会打印所有参数，确定参数中不会泄密后才能设置这个参数为true！！注解在类或者方法上都生效。
     * @return
     */
    boolean logAll() default false;

    /**
     * 禁用改日志打印
     * @return
     */
    boolean disableLog() default false;

    /**
     * 禁用指标统计
     * @return
     */
    boolean disableMetric() default false;

}
