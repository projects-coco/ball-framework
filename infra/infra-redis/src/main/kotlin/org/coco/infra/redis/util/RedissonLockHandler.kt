package org.coco.infra.redis.util

import org.coco.domain.util.LockHandler
import org.redisson.api.RedissonClient
import java.util.concurrent.TimeUnit

class RedissonLockHandler(
    val redissonClient: RedissonClient,
) : LockHandler {
    override fun tryLock(key: String, waitTime: Long, leaseTime: Long): Boolean {
        return redissonClient.getLock(key).tryLock(waitTime, leaseTime, TimeUnit.SECONDS)
    }

    override fun unlock(key: String): Boolean {
        val lock = redissonClient.getLock(key)
        if (lock == null || !lock.isHeldByCurrentThread) {
            return false
        }
        lock.unlock()
        return true
    }
}
