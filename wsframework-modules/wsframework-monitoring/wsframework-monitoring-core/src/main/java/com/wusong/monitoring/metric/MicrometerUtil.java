package com.wusong.monitoring.metric;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.lang.Nullable;
import org.apache.skywalking.apm.toolkit.opentracing.SkywalkingContext;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MicrometerUtil {

    private static ConcurrentHashMap<String, Tag> tagCache=new ConcurrentHashMap<>();
    private static final Tag EXCEPTION_NONE = tag("exception", "none");
    private static final String METHOD_WITH_URL_COUNT = "method_with_count";
    private static final String METHOD_WITH_URL_TIMER = "method_with_timer";
    public final static String EMPTY_STRING = "none";

    public static final Tag TAG_STATUS_KEY_TRUE = tag("status_key", "true");

    public static final Tag TAG_STATUS_KEY_FALSE = tag("status_key", "false");

    public static Tag exception(@Nullable Throwable exception) {
        TraceContext.traceId();
        if (exception == null) {
            return EXCEPTION_NONE;
        }
        String simpleName = exception.getClass().getSimpleName();
        return tag("exception", simpleName.isEmpty() ? exception.getClass().getName() : simpleName);
    }

    public static Tags exceptionAndStatusKey(@Nullable Throwable exception) {
        return Tags.of(exception(exception), statusKey(exception));
    }

    public static final long getNanosecondsAfter(long start) {
        return Metrics.globalRegistry.config().clock().monotonicTime() - start;
    }

    public static void methodCount(String methodName, Throwable throwable) {
        methodCount(methodName,throwable,Tags.empty());
    }

    public static void methodCount(String methodName, Throwable throwable,Tags otherTags) {
        Tags tags = tags("method", methodName).and(MicrometerUtil.exceptionAndStatusKey(throwable)).and(otherTags);
        Metrics.counter(METHOD_WITH_URL_COUNT, tags).increment();
    }

    public static Tags getTagsFromProceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint){
        return tags("class", proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),"classMethod", proceedingJoinPoint.getSignature().getName());
    }

    public static void methodTimer(String methodName, long time, Throwable throwable) {
        methodTimer(methodName,time,throwable,Tags.empty());
    }

    public static void methodTimer(String methodName, long time, Throwable throwable,Tags otherTags) {
        Tags tags = tags("method", methodName).and(MicrometerUtil.exceptionAndStatusKey(throwable)).and(otherTags);
        Metrics.timer(METHOD_WITH_URL_TIMER, tags).record(time, TimeUnit.MILLISECONDS);
    }

    public static final long monotonicTime() {
        return Metrics.globalRegistry.config().clock().monotonicTime();
    }

    public static final MeterRegistry registry() {
        return Metrics.globalRegistry;
    }

    public static Tag statusKey(@Nullable Throwable exception) {
        return exception == null ? TAG_STATUS_KEY_TRUE : TAG_STATUS_KEY_FALSE;
    }

    public static final Tag tag(String key, String value) {
        if (StringUtils.isEmpty(value)) {
            value = EMPTY_STRING;
        }
        String cacheKey = key + value;
        Tag catchTag = tagCache.get(cacheKey);
        if (null != catchTag) {
            return catchTag;
        }
        Tag tag = Tag.of(key, value);
        tagCache.put(cacheKey, tag);
        return tag;
    }

    public static final Tags tags(String... keyValues) {
        if (keyValues.length == 0) {
            return Tags.empty();
        }
        if (keyValues.length % 2 == 1) {
            throw new IllegalArgumentException("size must be even, it is a set of key=value pairs");
        }
        Tag[] tags = new Tag[keyValues.length / 2];
        for (int i = 0; i < keyValues.length; i += 2) {
            tags[i / 2] = tag(keyValues[i], keyValues[i + 1]);
        }
        return Tags.of(tags);
    }

}
