package org.coco.infra.spring.security

import arrow.core.Either
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import org.coco.core.utils.JsonUtils
import org.coco.core.utils.currentClock
import org.coco.domain.model.auth.Token
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.service.auth.TokenProvider
import org.coco.domain.service.auth.TokenProvider.VerifyError
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

typealias PrincipalBuilder = DecodedJWT.() -> UserPrincipal

class JwtTokenProvider(
    secret: String,
    private val issuer: String,
    private val expiry: Duration,
    private val toPrincipal: PrincipalBuilder,
) : TokenProvider {
    companion object {
        const val CLAIM_ID = "id"

        fun DecodedJWT.key(key: String): String {
            val raw = Base64.getDecoder().decode(this.payload).toString(StandardCharsets.UTF_8)
            val data = JsonUtils.deserialize(raw)
            return data[key] as String
        }
    }

    private val algorithm: Algorithm = Algorithm.HMAC512(secret)
    private val verifier = JWT.require(algorithm).withIssuer(issuer).build()

    override fun generateToken(payload: UserPrincipal): Token.Payload {
        return Token.Payload(
            JWT
                .create()
                .withExpiresAt(Date(System.currentTimeMillis() + expiry.toMillis()))
                .withIssuer(issuer)
                .withPayload(
                    mapOf(
                        "roles" to JsonUtils.serialize(payload.roles),
                        "username" to payload.username.value,
                    ),
                ).withClaim(CLAIM_ID, payload.name)
                .sign(algorithm),
        )
    }

    override fun verify(token: Token.Payload): Either<VerifyError, UserPrincipal> =
        Either.catch {
            verifier.verify(token.value).toPrincipal()
        }.mapLeft {
            mapVerifyError(it)
        }

    override fun getExpiredAt(token: Token.Payload): LocalDateTime {
        val decoded = JWT.decode(token.value)
        val expiredAt = decoded.expiresAt.toInstant()
        return LocalDateTime.ofInstant(expiredAt, currentClock().zone)
    }

    private fun mapVerifyError(it: Throwable): VerifyError =
        when (it) {
            is TokenExpiredException -> VerifyError.Expired
            is JWTVerificationException -> VerifyError.InvalidToken
            is IllegalStateException -> VerifyError.InvalidToken
            else -> VerifyError.InvalidToken
        }
}
