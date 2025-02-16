package org.coco.infra.spring.security

import arrow.core.Either
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.coco.core.utils.currentClock
import org.coco.domain.model.auth.Token
import org.coco.domain.service.auth.TokenProvider
import org.coco.domain.service.auth.TokenProvider.VerifyError
import java.nio.charset.StandardCharsets
import java.security.Principal
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import javax.crypto.SecretKey

abstract class TokenProviderJwtHelper<T : Principal>(
    secret: String,
    private val issuer: String,
    private val expiry: Duration,
    private val toPrincipal: Claims.() -> T,
    private val clock: Clock = currentClock()
) : TokenProvider<T> {
    companion object {
        const val CLAIM_ID = "id"
    }

    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
    private val parser = Jwts.parser()
        .verifyWith(key)
        .requireIssuer(issuer)
        .clock { Date.from(clock.instant()) }
        .build()

    abstract fun generatePayload(principal: T): Map<String, String>

    override fun generateToken(payload: T): Token.Payload {
        val expiration = Date.from(clock.instant().plus(expiry))
        val payloadClaims = generatePayload(payload).toMutableMap()
        payloadClaims[CLAIM_ID] = payload.name

        return Token.Payload(
            Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .issuer(issuer)
                .expiration(expiration)
                .claims(payloadClaims)
                .signWith(key)
                .compact()
        )
    }

    override fun getExpiredAt(token: Token.Payload): LocalDateTime {
        val claims = parser.parseSignedClaims(token.value).payload
        return LocalDateTime.ofInstant(claims.expiration.toInstant(), currentClock().zone)
    }

    override fun verify(token: Token.Payload): Either<VerifyError, T> =
        Either.catch {
            val claims = parser.parseSignedClaims(token.value).payload
            claims.toPrincipal()
        }.mapLeft { mapVerifyError(it) }

    private fun mapVerifyError(it: Throwable): VerifyError =
        when (it) {
            is ExpiredJwtException -> VerifyError.Expired
            is JwtException, is SecurityException, is IllegalArgumentException -> VerifyError.InvalidToken
            else -> VerifyError.InvalidToken
        }
}