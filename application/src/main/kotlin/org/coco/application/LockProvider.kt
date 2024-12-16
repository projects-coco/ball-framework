package org.coco.application

import java.util.concurrent.TimeUnit

abstract class LockProvider(
    private val txAdvice: TxAdvice,
) {
    companion object {
        const val LOCK_PREFIX = "LOCK:"
    }

    protected abstract fun tryLock(
        key: String,
        timeUnit: TimeUnit,
        waitTime: Long,
        leaseTime: Long,
    ): Boolean

    protected abstract fun unlock(key: String): Boolean

    fun <T> withLock(
        key: String,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        waitTime: Long = 10L,
        leaseTime: Long = 3L,
        action: () -> T,
    ) {
        val keyWithPrefix = "${LOCK_PREFIX}$key"
        try {
            val available = tryLock(key, timeUnit, waitTime, leaseTime)
            if (!available) {
                return
            }
            return txAdvice.runWithNewTransaction {
                action()
            }
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
            unlock(keyWithPrefix)
        }
    }
}
