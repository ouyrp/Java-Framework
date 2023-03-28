package com.wusong.web.security;

import com.wusong.crypt.web.EncryptResponseBodyAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author p14
 */
@RestControllerAdvice(annotations = EncryptedApi.class)
public class EncryptResponseBody extends EncryptResponseBodyAdvice {
}
