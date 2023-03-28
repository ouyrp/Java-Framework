package com.wusong.es;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 忽略 HTTPS 验证。内部有自签名的证书
 * @author p14
 */
@Slf4j
public class ESAutoConfiguration { private static SSLContext sslIgnoreContext;
    static {
        try {
            sslIgnoreContext=new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (Throwable e) {
            log.error("尝试禁用es ssl证书验证 失败",e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public RestClientBuilderCustomizer restClientBuilderCustomizerIgnoreSSL(){
        return new RestClientBuilderCustomizer() {
            @Override
            public void customize(RestClientBuilder builder) {
            }

            @Override
            public void customize(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                httpAsyncClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
                httpAsyncClientBuilder.setSSLContext(sslIgnoreContext);
                httpAsyncClientBuilder.setSSLStrategy(new SSLIOSessionStrategy(sslIgnoreContext, NoopHostnameVerifier.INSTANCE));
            }
        };
    }
}
