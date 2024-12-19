package org.coco.application.transaction

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.domain.exception.EntityUpdateError
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
class OptimisticLockRetryAspect {
    @Around("@annotation(org.coco.application.transaction.RetryOnOptimisticLock)")
    fun retryOptimisticLock(
        joinPoint: ProceedingJoinPoint,
        retryOnOptimisticLock: RetryOnOptimisticLock,
    ): Any {
        val maxRetries = retryOnOptimisticLock.maxRetries
        val delay = retryOnOptimisticLock.delay
        var retryAttempt = 0

        while (true) {
            try {
                return joinPoint.proceed()
            } catch (e: EntityUpdateError) {
                if (!shouldRetry(++retryAttempt, maxRetries)) {
                    throw LogicError(
                        "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.\n" +
                            "문제가 지속되면 관리자에게 문의해주세요.",
                        ErrorType.INTERNAL_SERVER_ERROR,
                    )
                }
                Thread.sleep(delay.toLong())
            }
        }
    }

    private fun shouldRetry(
        currentAttempt: Int,
        maxRetries: Int,
    ): Boolean = currentAttempt <= maxRetries
}
