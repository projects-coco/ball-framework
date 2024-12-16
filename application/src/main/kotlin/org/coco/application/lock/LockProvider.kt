package org.coco.application.lock

import org.coco.application.transaction.TxAdvice
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
    ): T {
        val keyWithPrefix = "$LOCK_PREFIX$key"
        return try {
            acquireLock(keyWithPrefix, timeUnit, waitTime, leaseTime)
            txAdvice.runWithNewTransaction(action)
        } finally {
            unlock(keyWithPrefix)
        }
    }

    private fun acquireLock(
        key: String,
        timeUnit: TimeUnit,
        waitTime: Long,
        leaseTime: Long,
    ) {
        val isLockAcquired = tryLock(key, timeUnit, waitTime, leaseTime)
        if (!isLockAcquired) {
            throw InterruptedException("Unable to acquire lock for key: $key")
        }
    }
}
