package com.wusong.uc.handler;

import com.wusong.uc.common.enums.AuthorizationCodeEnum;
import com.wusong.uc.common.exception.AuthorizationException;
import com.wusong.uc.utils.Cookies;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


/**
 * javadoc DefaultTokenHandler
 * <p>
 *     默认的 token 处理器
 *     主要处理token的读写
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/18 10:21 AM
 * @version 1.0.0
 **/
@SuppressWarnings(value = "unused")
public class DefaultTokenHandler implements ITokenHandler{

    /**
     * 先按照OAuth2.0标准处理
     * <link>https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication#authentication_schemes</link>
     * <link>https://datatracker.ietf.org/doc/html/rfc6750</link>
     **/
    public static final String AUTHORIZATION_KEY = "Authorization";

    /**
     * 先按照OAuth2.0标准处理
     * <link>https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication#authentication_schemes</link>
     * <link>https://datatracker.ietf.org/doc/html/rfc6750</link>
     **/
    public static final String AUTHORIZATION_VALUE_PREFIX = "Bearer ";

    public static final int AUTHORIZATION_VALUE_PREFIX_LENGTH = AUTHORIZATION_VALUE_PREFIX.length();

    @Override
    public String readToken(HttpServletRequest request, HttpServletResponse response) {
        final String bearer = Cookies.findFirst(AUTHORIZATION_KEY, request);
        if(Objects.isNull(bearer) || bearer.isEmpty()){
            throw new AuthorizationException(AuthorizationCodeEnum.UN_AUTH);
        }
        return bearer.substring(AUTHORIZATION_VALUE_PREFIX_LENGTH);
    }

    @Override
    public void writeToken(HttpServletResponse response, int expire, String token) {
        final Cookie cookie = new Cookie(AUTHORIZATION_KEY, AUTHORIZATION_VALUE_PREFIX + token);
        cookie.setMaxAge(expire);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }
}
