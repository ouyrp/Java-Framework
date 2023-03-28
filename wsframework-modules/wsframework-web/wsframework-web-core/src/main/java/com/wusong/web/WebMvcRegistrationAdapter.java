package com.wusong.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author p14
 */
@Configuration
@ConditionalOnClass(FeignClient.class)
@ConditionalOnProperty(name = "wsframework.mvc.handler.skip-feign",havingValue = "true",matchIfMissing = false)
public class WebMvcRegistrationAdapter implements WebMvcRegistrations {
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new RequestMappingHandlerMappingWithNotFeign();
    }

    public class RequestMappingHandlerMappingWithNotFeign extends RequestMappingHandlerMapping{
        @Override
        protected boolean isHandler(Class<?> beanType) {
            return AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) ||
                    (AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class)
                            && !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class));

        }
    }
}
