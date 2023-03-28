package com.wusong.uc.config;

import com.wusong.uc.handler.ITokenHandler;
import com.wusong.uc.interceptor.AuthorizationInterceptor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

/**
 * javadoc UcMvcConfig
 * <p>
 * spring mvc config
 * <p>
 *
 * @author weng xiaoyong
 * @version 1.0.0
 * @date 2021/5/11 11:10 AM
 **/
public class UcMvcConfig implements WebMvcConfigurer {

    @Setter(onMethod_ = @Autowired)
    private ITokenHandler tokenHandler;

    @Setter(onMethod_ = @Autowired)
    private UcSdkConfiguration configuration;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        final AuthorizationInterceptor interceptor = new AuthorizationInterceptor(tokenHandler);
        final InterceptorRegistration registration  = registry.addInterceptor(interceptor);
        final String [] patterns = StringUtils.split(configuration.getInterceptorsPathPatterns(), ",");
        if(Objects.nonNull(patterns) && patterns.length > 0){
            registration.addPathPatterns(patterns);
        }
        final String [] excludePatterns = StringUtils.split(configuration.getInterceptorsExcludePathPatterns(), ",");
        if(Objects.nonNull(excludePatterns) && excludePatterns.length > 0){
            registration.excludePathPatterns(excludePatterns);
        }
    }
}
