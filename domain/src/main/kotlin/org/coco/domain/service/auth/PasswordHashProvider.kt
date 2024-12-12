package org.coco.domain.service.auth

import org.coco.domain.model.user.BasicUser.IPassword
import org.coco.domain.model.user.BasicUser.PasswordHash

interface PasswordHashProvider {
    fun hash(password: IPassword): PasswordHash

    fun verify(
        password: IPassword,
        passwordHash: PasswordHash,
    ): Boolean
}
