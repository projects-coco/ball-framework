package org.coco.infra.spring.security

import io.jsonwebtoken.Claims
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.coco.core.type.BinaryId
import org.coco.core.utils.JsonUtils
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.vo.LegalName
import org.coco.domain.model.user.vo.Username
import org.coco.domain.service.auth.TokenProvider.VerifyError
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class JwtTokenProviderTest :
    FunSpec({
        val toPrincipal: Claims.() -> UserPrincipal = {
            UserPrincipal(
                id = BinaryId.fromString(this["id", String::class.java]),
                roles =
                JsonUtils
                    .deserialize(this["roles", String::class.java], Set::class.java)
                    .map { it.toString() }
                    .toSet(),
                username = Username(this["username", String::class.java]!!),
                legalName = LegalName(this["legalName", String::class.java]!!),
            )
        }
        val tokenProvider =
            UserPrincipalTokenProvider(
                issuer = "authProperties.issuer",
                secret = "authProperties.accessTokenSecret",
                expiry = Duration.ofMinutes(30),
                toPrincipal = toPrincipal,
            )

        test("DecodedJWT::payload(key: String)") {
            val id = BinaryId.new()
            val username = Username("username")
            val authToken =
                tokenProvider.generateToken(
                    UserPrincipal(
                        id = id,
                        roles = setOf("ROLE_USER"),
                        username = username,
                        legalName = LegalName("legalName"),
                    ),
                )

            val principal = tokenProvider.verify(authToken).shouldBeRight()

            principal.id.value shouldBe id.value
            principal.username shouldBe username
            principal.roles shouldBe setOf("ROLE_USER")
        }

        test("만료된 토큰 검증 시 VerifyError.Expired 반환") {
            val id = BinaryId.new()
            val username = Username("username")

            val fixedClock = Clock.fixed(
                Instant.now().minus(Duration.ofHours(2)),
                ZoneId.of("Asia/Seoul")
            )

            val tokenProviderWithFixedClock = UserPrincipalTokenProvider(
                issuer = "authProperties.issuer",
                secret = "authProperties.accessTokenSecret",
                expiry = Duration.ofMinutes(30),
                toPrincipal = toPrincipal,
                clock = fixedClock,
            )

            val authToken = tokenProviderWithFixedClock.generateToken(
                UserPrincipal(
                    id = id,
                    roles = setOf("ROLE_USER"),
                    username = username,
                    legalName = LegalName("legalName"),
                )
            )

            tokenProvider.verify(authToken).shouldBeLeft(VerifyError.Expired)
        }
    })
