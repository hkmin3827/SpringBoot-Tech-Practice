package springallinone.practice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("within(springallinone.practice..*Service)")
    public void serviceLayer() {}

    @Pointcut("within(springallinone.practice..*Controller)")
    public void controllerLayer() {}

    @Before("controllerLayer() || serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[{}] {} args = {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "result", pointcut = "serviceLayer()")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[{}] {} returned={}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(throwing = "ex", pointcut = "controllerLayer() || serviceLayer()")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        log.warn("[{}] {} threw={}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}
