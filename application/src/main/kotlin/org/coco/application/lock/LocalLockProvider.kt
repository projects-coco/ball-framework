package org.coco.application.lock

import org.coco.application.transaction.TxAdvice
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class LocalLockProvider(
    txAdvice: TxAdvice,
) : LockProvider(txAdvice) {
    companion object {
        val lockMap: MutableMap<String, Long> = mutableMapOf()
    }

    override fun tryLock(
        key: String,
        timeUnit: TimeUnit,
        waitTime: Long,
        leaseTime: Long,
    ): Boolean {
        val waitTimeInMillis = timeUnit.toMillis(waitTime)
        val startTime = System.currentTimeMillis()

        while (System.currentTimeMillis() - startTime < waitTimeInMillis) {
            synchronized(lockMap) {
                if (!lockMap.containsKey(key)) {
                    lockMap[key] = System.currentTimeMillis() + leaseTime
                    return true
                } else if (lockMap[key]!! < System.currentTimeMillis()) {
                    lockMap[key] = System.currentTimeMillis() + leaseTime
                    return true
                }
            }
            Thread.sleep(10)
        }
        return false
    }

    override fun unlock(key: String): Boolean {
        synchronized(lockMap) {
            if (lockMap.containsKey(key)) {
                lockMap.remove(key)
                return true
            }
        }
        return false
    }
}
