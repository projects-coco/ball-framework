package org.coco.core.type

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
