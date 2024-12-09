package org.coco.domain.core

import org.coco.domain.model.BinaryId

object BallRequestContext {
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
