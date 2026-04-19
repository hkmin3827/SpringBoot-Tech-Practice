package springallinone.practice.concurrency.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import springallinone.practice.concurrency.annotation.DistributedLock;
import springallinone.practice.concurrency.exception.DistributedLockAcquisitionFailException;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private static final String LOCK_PREFIX = "lock:";

    private final RedissonClient redissonClient;

    @Around("annotation(springallinone.practice.concurrency.annotation.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String lockKey = LOCK_PREFIX + distributedLock.key();
        RLock lock = redissonClient.getLock(lockKey);

        boolean acquired = false;
        try {
            acquired = lock.tryLock(
                    distributedLock.waitTime(),
                    distributedLock.leaseTime(),
                    distributedLock.timeUnit()
            );

            if (!acquired) {
                log.warn("락 획득 실패 - key: {}, waitTime: {}{}",
                        lockKey, distributedLock.waitTime(), distributedLock.timeUnit());
                throw new DistributedLockAcquisitionFailException(lockKey);
            }

            log.debug("락 획득 성공 - key: {}", lockKey);
            return joinPoint.proceed();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("락 대기 중 인터럽트 발생 - key: {}", lockKey, e);
            throw new DistributedLockAcquisitionFailException(lockKey, e);

        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("락 해제 - key: {}", lockKey);
            }
        }
    }
}
