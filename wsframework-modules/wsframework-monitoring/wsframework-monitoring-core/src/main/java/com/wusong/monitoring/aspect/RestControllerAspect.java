package com.wusong.monitoring.aspect;

import com.wusong.monitoring.MonitoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author p14
 */
@Slf4j
@Aspect
public class RestControllerAspect extends AbstractLogAspect {

    public RestControllerAspect(MonitoringProperties monitoringProperties) {
        super(monitoringProperties);
    }

    @Pointcut("within(@com.wusong.monitoring.Monitoring *)")
    public void monitoringPointcut() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {
    }

    @Around("restControllerPointcut() && !monitoringPointcut()")
    public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return super.around(proceedingJoinPoint);
    }

}
