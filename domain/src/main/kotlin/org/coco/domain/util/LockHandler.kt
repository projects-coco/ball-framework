package org.coco.domain.util

interface LockHandler {
    fun tryLock(key: String, waitTime: Long, leaseTime: Long): Boolean

    fun unlock(key: String): Boolean
}
