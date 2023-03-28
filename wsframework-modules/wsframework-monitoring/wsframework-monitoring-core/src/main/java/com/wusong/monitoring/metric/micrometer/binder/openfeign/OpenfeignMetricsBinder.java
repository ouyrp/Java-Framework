package com.wusong.monitoring.metric.micrometer.binder.openfeign;

import com.wusong.monitoring.metric.MicrometerUtil;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyList;

@Aspect
public class OpenfeignMetricsBinder {
    public static final String CLASS_HEADER = "x-call-class";
    public static final String METHOD_HEADER = "x-call-method";

    private static String getUrl(String url) {
        int index = url.indexOf("?");
        if (index > 0) {
            url = url.substring(0, index);
        }
        return url;
    }

    public static void main(String[] args) {
        System.out.println(getUrl("a"));
        System.out.println(getUrl("a?a=3"));
    }

    private final Iterable<Tag> tags;

    public OpenfeignMetricsBinder() {
        this(emptyList());
    }

    public OpenfeignMetricsBinder(Iterable<Tag> tags) {
        this.tags = tags;
    }

    @Around("@within(org.springframework.cloud.openfeign.FeignClient)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long start = MicrometerUtil.monotonicTime();
        Object response = null;
        Throwable e = null;
        try {
            response = pjp.proceed();
        } catch (Throwable t) {
            throw e = t;
        } finally {
            long lapsed = MicrometerUtil.monotonicTime() - start;
            Timer timer = Metrics.timer("openfeign",
                    Tags.of(tags)
                            .and(MicrometerUtil.getTagsFromProceedingJoinPoint(pjp)).and(MicrometerUtil.exceptionAndStatusKey(e)));
            timer.record(lapsed, TimeUnit.NANOSECONDS);
        }
        return response;
    }

    private String getKey(String key, Map<String, Collection<String>> headers) {
        if (headers.containsKey(key)) {
            return headers.get(key).stream().findFirst().orElseGet(() -> "none");
        }
        return "none";
    }
}
