package com.wusong.web;

import com.wusong.web.exception.WsFrameworkGlobalExceptionHandler;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author p14
 */
@AutoConfigureAfter(OkhttpAutoConfiguration.class)
@Import({WebMvcRegistrationAdapter.class,WebAutoConfiguration.OkHttpRestTemplate.class})
public class WebAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "wsframework.exception.global.handler.enable",matchIfMissing = true,havingValue = "true")
    public WsFrameworkGlobalExceptionHandler globalExceptionHandler(){
        return new WsFrameworkGlobalExceptionHandler();
    }

    @Configuration
    @ConditionalOnClass(OkHttpClient.class)
    @ConditionalOnBean(OkHttpClient.class)
    @ConditionalOnMissingBean(RestTemplate.class)
    public static class OkHttpRestTemplate {
        @Bean
        public OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory(OkHttpClient okHttpClient){
            return new OkHttp3ClientHttpRequestFactory(okHttpClient);
        }

        @Bean
        public RestTemplate restTemplate(OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory){
            return new RestTemplate(okHttp3ClientHttpRequestFactory);
        }
    }
}
