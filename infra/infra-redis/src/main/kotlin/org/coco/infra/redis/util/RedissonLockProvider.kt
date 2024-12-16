package org.coco.infra.redis.util

import org.coco.application.lock.LockProvider
import org.coco.application.transaction.TxAdvice
import org.redisson.api.RedissonClient
import org.springframework.context.annotation.Primary
import java.util.concurrent.TimeUnit

@Primary
class RedissonLockProvider(
    txAdvice: TxAdvice,
    val redissonClient: RedissonClient,
) : LockProvider(txAdvice = txAdvice) {
    override fun tryLock(
        key: String,
        timeUnit: TimeUnit,
        waitTime: Long,
        leaseTime: Long,
    ): Boolean = redissonClient.getLock(key).tryLock(waitTime, leaseTime, timeUnit)

    override fun unlock(key: String): Boolean {
        val lock = redissonClient.getLock(key)
        if (lock == null || !lock.isHeldByCurrentThread) {
            return false
        }
        lock.unlock()
        return true
    }
}
