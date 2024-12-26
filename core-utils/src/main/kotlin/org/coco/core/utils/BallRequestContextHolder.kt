package org.coco.core.utils

import org.coco.core.type.BinaryId

object BallRequestContextHolder {
    private val currentRequestId = ThreadLocal<BinaryId>()

    var requestId: BinaryId
        get() = currentRequestId.get()
        private set(requestId) {
            currentRequestId.set(requestId)
        }

    fun allocateRequestId() {
        requestId = BinaryId.new()
    }

    fun deallocateRequestId() {
        currentRequestId.remove()
    }
}
