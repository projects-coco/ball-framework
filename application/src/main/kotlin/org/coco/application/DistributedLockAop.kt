package org.coco.application

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.coco.domain.util.LockHandler
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Aspect
@Component
class DistributedLockAop(
    val lockHandler: LockHandler,
    val txAdvice: TxAdvice,
) {
    val REDISSON_LOCK_PREFIX: String = "LOCK:"

    @Around("@annotation(org.coco.application.DistributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any {
        val signature: MethodSignature = joinPoint.signature as MethodSignature
        val method: Method = signature.getMethod()
        val distributedLock: DistributedLock = method.getAnnotation(DistributedLock::class.java)

        val key: String = REDISSON_LOCK_PREFIX + distributedLock.key

        try {
            val available = lockHandler.tryLock(key, distributedLock.waitTime, distributedLock.leaseTime)
            if (!available) {
                return false
            }
            return txAdvice.runWithNewTransaction {
                joinPoint.proceed()
            }
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
            lockHandler.unlock(key)
        }
    }
}