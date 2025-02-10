package org.coco.domain.service.auth

import arrow.core.Either
import org.coco.domain.model.auth.Token.Payload
import java.security.Principal
import java.time.LocalDateTime

interface TokenProvider<T : Principal> {
    fun generateToken(payload: T): Payload

    sealed interface VerifyError {
        data object InvalidToken : VerifyError

        data object Expired : VerifyError
    }

    fun verify(token: Payload): Either<VerifyError, T>

    fun getExpiredAt(token: Payload): LocalDateTime
}
