package org.coco.infra.spring.security

import org.coco.domain.model.user.BasicUser.IPassword
import org.coco.domain.model.user.BasicUser.PasswordHash
import org.coco.domain.service.auth.PasswordHashProvider
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

class Argon2HashProvider : PasswordHashProvider {
    private val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

    override fun hash(password: IPassword): PasswordHash = PasswordHash(encoder.encode(password.value))

    override fun verify(
        password: IPassword,
        passwordHash: PasswordHash,
    ): Boolean = encoder.matches(password.value, passwordHash.value)
}
