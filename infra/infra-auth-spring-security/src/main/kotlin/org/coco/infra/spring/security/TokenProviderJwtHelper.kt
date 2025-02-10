package org.coco.infra.spring.security

import arrow.core.Either
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import org.coco.core.utils.JsonUtils
import org.coco.core.utils.currentClock
import org.coco.domain.model.auth.Token
import org.coco.domain.service.auth.TokenProvider
import org.coco.domain.service.auth.TokenProvider.VerifyError
import java.nio.charset.StandardCharsets
import java.security.Principal
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

abstract class TokenProviderJwtHelper<T : Principal>(
    secret: String,
    private val issuer: String,
    private val expiry: Duration,
    private val toPrincipal: DecodedJWT.() -> T,
) : TokenProvider<T> {
    companion object {
        const val CLAIM_ID = "id"

        fun DecodedJWT.key(key: String): String {
            val raw = Base64.getDecoder().decode(this.payload).toString(StandardCharsets.UTF_8)
            val data = JsonUtils.deserialize(raw)
            return data[key] as String
        }
    }

    private val algorithm: Algorithm = Algorithm.HMAC512(secret)
    private val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(issuer).build()

    abstract fun generatePayload(principal: T): Map<String, String>

    override fun generateToken(payload: T): Token.Payload =
        Token.Payload(
            JWT
                .create()
                .withExpiresAt(Date(System.currentTimeMillis() + expiry.toMillis()))
                .withIssuer(issuer)
                .withPayload(generatePayload(payload))
                .withClaim(CLAIM_ID, payload.name)
                .sign(algorithm),
        )

    override fun getExpiredAt(token: Token.Payload): LocalDateTime {
        val decoded = JWT.decode(token.value)
        val expiredAt = decoded.expiresAt.toInstant()
        return LocalDateTime.ofInstant(expiredAt, currentClock().zone)
    }

    override fun verify(token: Token.Payload): Either<VerifyError, T> =
        Either
            .catch {
                verifier.verify(token.value).toPrincipal()
            }.mapLeft {
                mapVerifyError(it)
            }

    private fun mapVerifyError(it: Throwable): VerifyError =
        when (it) {
            is TokenExpiredException -> VerifyError.Expired
            is JWTVerificationException -> VerifyError.InvalidToken
            is IllegalStateException -> VerifyError.InvalidToken
            else -> VerifyError.InvalidToken
        }
}
