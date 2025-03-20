package org.coco.domain.model.auth

object SystemFlagContextHolder {
    private val context = ThreadLocal<Boolean>()

    var systemFlag: Boolean?
        get() = context.get()
        private set(value) = context.set(value)

    fun set(systemFlag: Boolean) {
        context.set(systemFlag)
    }

    fun clear() = context.remove()
}
