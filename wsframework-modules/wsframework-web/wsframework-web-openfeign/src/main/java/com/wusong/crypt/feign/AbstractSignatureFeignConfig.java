package com.wusong.crypt.feign;

import com.wusong.crypt.common.AESEncryptor;
import com.wusong.crypt.common.AuthConstants;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.FeignEncoderProperties;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wusong.crypt.common.AuthConstants.HEADER_SIGN;

/**
 * @author p14
 */
public abstract class AbstractSignatureFeignConfig {

    /**
     * 服务提供方分配，可公开
     * @return
     */
    protected abstract String getAccessKey();

    /**
     * 服务提供方分配，需保密
     * @return
     */
    protected abstract String getSecretKey();

    /**
     * salt 值需要和服务提供方保持一致
     * @return
     */
    protected abstract String getSalt();

    /**
     * 是否启用加解密
     * @return
     */
    protected abstract boolean encrypt();

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Autowired(required = false)
    private FeignEncoderProperties encoderProperties;

    /**
     * 对请求进行签名和加密
     * @param aesEncryptor
     * @return
     */
    @Bean
    public FeignSignatureInterceptor feignSignatureInterceptor(AESEncryptor aesEncryptor){
        return new FeignSignatureInterceptor(getAccessKey(),getSecretKey(),encrypt()?aesEncryptor:null);
    }

    @Bean
    public AESEncryptor aesEncryptor(){
        return new AESEncryptor(getSalt());
    }

    /**
     * 解密服务端返回值
     * @param aesEncryptor
     * @return
     */
    @Bean
    public Decoder feignDecoder(AESEncryptor aesEncryptor) {
        return new OptionalDecoder(new ResponseEntityDecoder(new DecryptSpringDecoder(encrypt()?aesEncryptor:null,getSecretKey(),this.messageConverters)));
    }

    private static class DecryptSpringDecoder implements Decoder {

        private ObjectFactory<HttpMessageConverters> messageConverters;
        private AESEncryptor aesEncryptor;
        private String sk;

        public DecryptSpringDecoder(AESEncryptor aesEncryptor, String sk, ObjectFactory<HttpMessageConverters> messageConverters) {
            this.messageConverters = messageConverters;
            this.aesEncryptor=aesEncryptor;
            this.sk=sk;
        }

        @Override
        public Object decode(final Response response, Type type) throws IOException, FeignException {
            if (type instanceof Class || type instanceof ParameterizedType || type instanceof WildcardType) {
                @SuppressWarnings({ "unchecked", "rawtypes" })
                HttpMessageConverterExtractor<?> extractor = new HttpMessageConverterExtractor(type,
                        this.messageConverters.getObject().getConverters());

                return extractor.extractData(new FeignResponseAdapter(aesEncryptor,sk,response));
            }
            throw new DecodeException(response.status(), "type is not an instance of Class or ParameterizedType: " + type,
                    response.request());
        }

        private static class FeignResponseAdapter implements ClientHttpResponse {

            private final Response response;
            private AESEncryptor aesEncryptor;
            private String sk;

            private FeignResponseAdapter(AESEncryptor aesEncryptor, String sk, Response response) {
                this.response = response;
                this.aesEncryptor=aesEncryptor;
                this.sk=sk;
            }

            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.valueOf(this.response.status());
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return this.response.status();
            }

            @Override
            public String getStatusText() throws IOException {
                return this.response.reason();
            }

            @Override
            public void close() {
                try {
                    this.response.body().close();
                }
                catch (IOException ex) {
                    // Ignore exception on close...
                }
            }
            private  String body;

            @Override
            public InputStream getBody() throws IOException {
                if(body==null){
                    body = IOUtils.readLines(this.response.body().asInputStream()).stream().collect(Collectors.joining("\n"));
                    if(aesEncryptor!=null&&response.headers().containsKey(HEADER_SIGN)){
                        body = aesEncryptor.decryptGCM(sk, body);
                    }
                }
                return new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public HttpHeaders getHeaders() {
                return getHttpHeaders(this.response.headers());
            }

            static HttpHeaders getHttpHeaders(Map<String, Collection<String>> headers) {
                HttpHeaders httpHeaders = new HttpHeaders();
                for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
                    httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
                }
                return httpHeaders;
            }
        }

    }

}
