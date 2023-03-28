package com.wusong.monitoring.aspect;

import com.wusong.monitoring.Monitoring;
import com.wusong.monitoring.MonitoringProperties;
import com.wusong.monitoring.metric.MicrometerUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

/**
 * @author p14
 */
@Slf4j
public abstract class AbstractLogAspect {

    private MonitoringProperties monitoringProperties;

    @Autowired(required = false)
    private LoginUserNameProvider loginUserNameProvider;

    private String logUserName(){
        if(loginUserNameProvider!=null){
            return String.format("LOGIN=[%s] ",loginUserNameProvider.getLoginUserName());
        }else {
            return "";
        }
    }

    public AbstractLogAspect(MonitoringProperties monitoringProperties) {
        this.monitoringProperties = monitoringProperties;
    }

    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            Monitoring monitoring = getAnnotationFromMethodOrClass(proceedingJoinPoint, Monitoring.class);
            Object result = null;
            Throwable throwable = null;
            boolean needLog =monitoring==null|| !monitoring.disableLog();
            if(needLog){
                doNotThrow(()->{
                    logBefore(monitoring,proceedingJoinPoint);
                });
            }
            long begin=System.nanoTime();
            try {
                result=proceedingJoinPoint.proceed();
            } catch (Throwable e) {
                throwable=e;
            }
            long end=System.nanoTime();
            long costMills=(end-begin)/1000_000;
            Throwable finalThrowable = throwable;
            Object finalResult = result;
            if(needLog){
                doNotThrow(()->{
                    logAfter(monitoring,proceedingJoinPoint, finalResult, finalThrowable,costMills);
                });
            }
            doNotThrow(()->{
                MicrometerUtil.methodTimer(getClassAndMethodName(proceedingJoinPoint),costMills, finalThrowable);
            });

            if(throwable!=null){
                throw throwable;
            }
            return finalResult;
    }

    private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private ExpressionParser parser = new SpelExpressionParser();

    private boolean logAll(Monitoring monitoring){
        return monitoringProperties.isLogAll()|| (monitoring != null && monitoring.logAll());
    }

    private String getClassAndMethodName(ProceedingJoinPoint proceedingJoinPoint){
        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
        return proceedingJoinPoint.getTarget().getClass().getSimpleName()+"."+methodSignature.getName();
    }

    private void logBefore(@Nullable Monitoring monitoring, ProceedingJoinPoint proceedingJoinPoint){
        Logger targetLog= getTargetLogger(monitoring,proceedingJoinPoint);
        if(targetLog.isInfoEnabled()){
            MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
            Object[] argValues = proceedingJoinPoint.getArgs();
            String[] argNames = methodSignature.getParameterNames();
            if(logAll(monitoring)){
                StringBuilder logArgs=new StringBuilder();
                for (int i = 0; i < argNames.length; i++) {
                    logArgs.append(argNames[i]).append("=").append(argValues[i]);
                    logArgs.append(", ");
                }
                targetLog.info("{} {} called with arguments {}",logUserName(),getClassAndMethodName(proceedingJoinPoint),logArgs);
            }else if(monitoring!=null&& StringUtils.hasText(monitoring.logMethodParameter())) {
                EvaluationContext context = new MethodBasedEvaluationContext(null, ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod(), proceedingJoinPoint.getArgs(), nameDiscoverer);
                Object arguments = parser.parseExpression(monitoring.logMethodParameter()).getValue(context);
                targetLog.info("{} {} called with arguments {}",logUserName(),getClassAndMethodName(proceedingJoinPoint),arguments);
            }else {
                targetLog.info("{} {} called",logUserName(),getClassAndMethodName(proceedingJoinPoint));
            }
        }
    }

    private Logger getTargetLogger(@Nullable Monitoring monitoring, ProceedingJoinPoint proceedingJoinPoint){
        if(monitoring!=null&&StringUtils.hasText(monitoring.value())){
            return LoggerFactory.getLogger(monitoring.value());
        }else {
            return LoggerFactory.getLogger( proceedingJoinPoint.getTarget().getClass());
        }
    }

    private void logAfter(Monitoring monitoring, ProceedingJoinPoint proceedingJoinPoint, Object finalResult, Throwable finalThrowable, long cost){

        Logger targetLog= getTargetLogger(monitoring,proceedingJoinPoint);
        if(targetLog.isInfoEnabled()){
            MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
            if(logAll(monitoring)){
                targetLog.info("{} {} returned in [{}] ms {}",logUserName(),getClassAndMethodName(proceedingJoinPoint),cost,finalResult);
            }else if(monitoring!=null&&StringUtils.hasText(monitoring.logReturnedValue())) {
                StandardEvaluationContext standardEvaluationContext=new StandardEvaluationContext();
                standardEvaluationContext.setVariable("return",finalResult);
                Object arguments = parser.parseExpression(monitoring.logReturnedValue()).getValue(standardEvaluationContext);
                targetLog.info("{} {} returned in [{}] ms {}",logUserName(),getClassAndMethodName(proceedingJoinPoint),cost,arguments);
            }else {
                targetLog.info("{} {} returned in [{}] ms",logUserName(),getClassAndMethodName(proceedingJoinPoint),cost);
            }
            if(finalThrowable!=null){
                targetLog.error(finalThrowable.getLocalizedMessage(),finalThrowable);
            }
        }
    }

    private void doNotThrow(Runnable runnable){
        try {
            runnable.run();
        }catch (Throwable t){
            log.error(t.getLocalizedMessage(),t);
        }
    }

    private MethodSignature getMethodSignature(ProceedingJoinPoint proceedingJoinPoint){
        return (MethodSignature) proceedingJoinPoint.getSignature();
    }


    public <T extends Annotation> T getAnnotationFromMethodOrClass(ProceedingJoinPoint proceedingJoinPoint, Class<T> clazz) {
        T ann = getMethodSignature(proceedingJoinPoint).getMethod().getAnnotation(clazz);
        if (ann != null) {
            return ann;
        }
        return proceedingJoinPoint.getTarget().getClass().getAnnotation(clazz);
    }

}
