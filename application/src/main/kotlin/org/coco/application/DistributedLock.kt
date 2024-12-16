package org.coco.application

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(
    val key: String,
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
    val waitTime: Long = 10L,
    val leaseTime: Long = 3L
)

@Aspect
@Component
class DistributedLockAop(
    val redissonClient: RedissonClient,
    val txForAop: TxForAop
) {
    val REDISSON_LOCK_PREFIX: String = "LOCK:"

    @Around("@annotation(org.coco.application.DistributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any {
        val signature: MethodSignature = joinPoint.signature as MethodSignature
        val method: Method = signature.getMethod()
        val distributedLock: DistributedLock = method.getAnnotation(DistributedLock::class.java)

        val key: String = REDISSON_LOCK_PREFIX + distributedLock.key
        val rLock = redissonClient.getLock(key)

        try {
            val available = rLock.tryLock(distributedLock.waitTime, distributedLock.leaseTime, distributedLock.timeUnit)
            if (!available) {
                return false
            }
            return txForAop.proceed(joinPoint)
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
            if (rLock != null && rLock.isHeldByCurrentThread) {
                rLock.unlock()
            }
        }
    }
}

@Component
class TxForAop {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun proceed(joinPoint: ProceedingJoinPoint): Any {
        return joinPoint.proceed()
    }
}
