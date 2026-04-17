package springallinone.practice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import springallinone.practice.aop.annotation.TraceTime;

@Aspect
@Component
@Slf4j
public class TimeTraceAop {

    @Around("@annotation(traceTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, TraceTime traceTime) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long execTime = System.currentTimeMillis() - start;
        log.info("Execution time: {} ms / methodName = {}", execTime, joinPoint.getSignature().getName());
        return result;
    }

}
