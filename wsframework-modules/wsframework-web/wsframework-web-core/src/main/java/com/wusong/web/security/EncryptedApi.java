package com.wusong.web.security;

import java.lang.annotation.*;

/**
 * 加上该注解后，框架会自动解密request body，并加密response body
 * @author p14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptedApi {
}
