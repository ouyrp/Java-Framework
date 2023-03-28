package com.wusong.monitoring.metric.micrometer.binder.resttemplate;

import com.wusong.monitoring.metric.MicrometerUtil;
import io.micrometer.core.instrument.*;
import io.micrometer.core.lang.Nullable;
import org.springframework.boot.actuate.metrics.web.client.DefaultRestTemplateExchangeTagsProvider;
import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTags;
import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTagsProvider;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.util.UriTemplateHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MetricsClientHttpRequestInterceptor
    implements ClientHttpRequestInterceptor, AsyncClientHttpRequestInterceptor {

    private static final ThreadLocal<String> urlTemplateHolder = new NamedThreadLocal<>("Rest Template URL Template");

    private final MeterRegistry meterRegistry;
    private final RestTemplateExchangeTagsProvider tagProvider = new DefaultRestTemplateExchangeTagsProvider(){
        @Override
        public Iterable<Tag> getTags(String urlTemplate, HttpRequest request, ClientHttpResponse response) {
            Tag uriTag = (StringUtils.hasText(urlTemplate)
                    ? RestTemplateExchangeTags.uri(shortUri(urlTemplate))
                    : RestTemplateExchangeTags.uri(shortUri(request.getURI().getPath())));
            return Arrays.asList(RestTemplateExchangeTags.method(request), uriTag,
                    RestTemplateExchangeTags.status(response),
                    RestTemplateExchangeTags.clientName(request));
        }
        private String shortUri(String uri){
            if(uri==null){
                return "";
            }
            int index=uri.indexOf("?");
            if(index>0){
                return uri.substring(0,index);
            }
            return uri;
        }
    };

    private final String metricName;

    public MetricsClientHttpRequestInterceptor(MeterRegistry meterRegistry, String metricName) {
        this.meterRegistry = meterRegistry;
        this.metricName = metricName;
    }

    UriTemplateHandler createUriTemplateHandler(UriTemplateHandler delegate) {
        return new UriTemplateHandler() {

            @Override
            public URI expand(String url, Map<String, ?> arguments) {
                urlTemplateHolder.set(url);
                return delegate.expand(url, arguments);
            }

            @Override
            public URI expand(String url, Object... arguments) {
                urlTemplateHolder.set(url);
                return delegate.expand(url, arguments);
            }
        };
    }

    private Timer.Builder getTimeBuilder(@Nullable String urlTemplate, HttpRequest request,
        @Nullable ClientHttpResponse response, @Nullable Throwable e) {
        return Timer.builder(this.metricName).tags(Tags.concat(this.tagProvider.getTags(urlTemplate, request, response),
            MicrometerUtil.exceptionAndStatusKey(e))).description("Timer of RestTemplate operation");
    }

    @Override
    public ListenableFuture<ClientHttpResponse> intercept(HttpRequest request, byte[] body,
        AsyncClientHttpRequestExecution execution) throws IOException {
        final String urlTemplate = urlTemplateHolder.get();
        urlTemplateHolder.remove();
        final Clock clock = meterRegistry.config().clock();
        final long startTime = clock.monotonicTime();
        ListenableFuture<ClientHttpResponse> future;
        try {
            future = execution.executeAsync(request, body);
        } catch (IOException e) {
            getTimeBuilder(urlTemplate, request, null, e).register(meterRegistry)
                .record(clock.monotonicTime() - startTime, TimeUnit.NANOSECONDS);
            throw e;
        }
        future.addCallback(new ListenableFutureCallback<ClientHttpResponse>() {
            @Override
            public void onFailure(final Throwable ex) {
                getTimeBuilder(urlTemplate, request, null, ex).register(meterRegistry)
                    .record(clock.monotonicTime() - startTime, TimeUnit.NANOSECONDS);
            }

            @Override
            public void onSuccess(final ClientHttpResponse response) {
                getTimeBuilder(urlTemplate, request, response, null).register(meterRegistry)
                    .record(clock.monotonicTime() - startTime, TimeUnit.NANOSECONDS);
            }
        });
        return future;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException {
        final String urlTemplate = urlTemplateHolder.get();
        urlTemplateHolder.remove();
        final Clock clock = meterRegistry.config().clock();
        final long startTime = clock.monotonicTime();
        IOException ex = null;
        ClientHttpResponse response = null;
        try {
            response = execution.execute(request, body);
            return response;
        } catch (IOException e) {
            ex = e;
            throw e;
        } finally {
            getTimeBuilder(urlTemplate, request, response, ex).register(this.meterRegistry)
                .record(clock.monotonicTime() - startTime, TimeUnit.NANOSECONDS);
        }
    }
}
