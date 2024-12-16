package org.coco.application

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Aspect
@Component
class DistributedLockAop(
    val lockProvider: LockProvider,
) {
    @Around("@annotation(org.coco.application.DistributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any {
        val signature: MethodSignature = joinPoint.signature as MethodSignature
        val method: Method = signature.method
        val distributedLock: DistributedLock = method.getAnnotation(DistributedLock::class.java)

        return lockProvider.withLock(
            key = distributedLock.key,
            timeUnit = distributedLock.timeUnit,
            waitTime = distributedLock.waitTime,
            leaseTime = distributedLock.leaseTime,
        ) {
            joinPoint.proceed()
        }
    }
}
