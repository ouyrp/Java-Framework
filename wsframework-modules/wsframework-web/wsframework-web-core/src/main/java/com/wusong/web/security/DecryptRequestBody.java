package com.wusong.web.security;

import com.wusong.crypt.web.DecryptRequestBodyAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author p14
 */
@RestControllerAdvice(annotations = EncryptedApi.class)
public class DecryptRequestBody extends DecryptRequestBodyAdvice {
}
