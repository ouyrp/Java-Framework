package com.wusong.uc.annos;

import com.wusong.uc.config.UcSdkAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(UcSdkAutoConfiguration.class)
public @interface EnableUcSdk {

}
