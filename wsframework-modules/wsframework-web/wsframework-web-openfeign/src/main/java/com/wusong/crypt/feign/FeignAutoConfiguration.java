package com.wusong.crypt.feign;

import com.wusong.web.OkhttpAutoConfiguration;
import feign.Client;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

/**
 * @author p14
 */
@AutoConfigureAfter(OkhttpAutoConfiguration.class)
@Import({FeignAutoConfiguration.OkHttp.class})
public class FeignAutoConfiguration {

    @Configuration
    @ConditionalOnClass(okhttp3.OkHttpClient.class)
    @ConditionalOnBean(okhttp3.OkHttpClient.class)
    public static class OkHttp{
        @Bean
        @ConditionalOnMissingBean(Client.class)
        public Client feignClient(okhttp3.OkHttpClient client, FeignHttpClientProperties httpClientProperties) {
            return new feign.okhttp.OkHttpClient(client.newBuilder()
                    .connectTimeout(httpClientProperties.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                    .build());
        }
    }
}
