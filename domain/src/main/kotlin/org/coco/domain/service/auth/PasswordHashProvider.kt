package org.coco.domain.service.auth

import org.coco.domain.model.user.vo.IPassword
import org.coco.domain.model.user.vo.PasswordHash

interface PasswordHashProvider {
    fun hash(password: IPassword): PasswordHash

    fun verify(
        password: IPassword,
        passwordHash: PasswordHash,
    ): Boolean
}
