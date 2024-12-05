package org.coco.domain.service.auth

import arrow.core.Either
import org.coco.domain.model.auth.Token.Payload
import org.coco.domain.model.auth.UserPrincipal
import java.time.LocalDateTime

interface TokenProvider {
    fun generateToken(payload: UserPrincipal): Payload

    sealed interface VerifyError {
        data object InvalidToken : VerifyError

        data object Expired : VerifyError
    }

    fun verify(token: Payload): Either<VerifyError, UserPrincipal>

    fun getExpiredAt(token: Payload): LocalDateTime
}