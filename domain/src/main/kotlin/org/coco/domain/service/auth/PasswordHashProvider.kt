package org.coco.domain.service.auth

interface PasswordHashProvider<T, H> {
    fun hash(password: T): H

    fun verify(
        password: T,
        passwordHash: H,
    ): Boolean
}
