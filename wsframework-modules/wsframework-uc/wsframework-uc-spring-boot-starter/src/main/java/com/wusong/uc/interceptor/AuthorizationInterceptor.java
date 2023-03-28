package com.wusong.uc.interceptor;

import com.wusong.uc.auth.Auths;
import com.wusong.uc.common.exception.AuthorizationException;
import com.wusong.uc.handler.ITokenHandler;
import com.wusong.uc.passport.Passports;
import com.wusong.uc.passport.domain.TokenPayloadBo;
import com.wusong.web.dto.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * javadoc AuthorizationInterceptor
 * <p>
 *     请求认证拦截器
 * <p>
 * @author weng xiaoyong
 * @date 2021/8/19 4:42 PM
 * @version 1.0.0
 **/
@SuppressWarnings(value = {"UnusedParameters", "NullableProblems"})
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {


    private final ITokenHandler tokenHandler;



    public AuthorizationInterceptor(ITokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        final String url = request.getRequestURI();
        final String token = tokenHandler.readToken(request, response);
        final ApiResult<TokenPayloadBo> result =  Passports.parseToken(token);
        if(!result.successful() || Objects.isNull(result.getData())){
            log.error("HandlerInterceptor.preHandle( url = [{}], token = [{}] ) 调用Passports.parseToken() 结果失败: [{}]", url, token, result);
            throw new AuthorizationException(result.getCode(), result.getMessage());
        }
        Auths.setToken(result.getData());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        Auths.removeToken();
    }
}
