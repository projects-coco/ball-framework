package org.coco.domain.service.auth

import org.coco.domain.model.user.BasicUser.Password
import org.coco.domain.model.user.BasicUser.PasswordHash

interface PasswordHashProvider {
    fun hash(password: Password): PasswordHash

    fun verify(
        password: Password,
        passwordHash: PasswordHash,
    ): Boolean
}
