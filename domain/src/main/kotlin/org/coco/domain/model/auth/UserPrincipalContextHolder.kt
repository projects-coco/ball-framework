package org.coco.domain.model.auth

object UserPrincipalContextHolder {
    private val context = ThreadLocal<UserPrincipal>()

    var userPrincipal: UserPrincipal?
        get() = context.get()
        private set(value) = context.set(value)

    fun set(userPrincipal: UserPrincipal) {
        context.set(userPrincipal)
    }

    fun clear() = context.remove()
}
