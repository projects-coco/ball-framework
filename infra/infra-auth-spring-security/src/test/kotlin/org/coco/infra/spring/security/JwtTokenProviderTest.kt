package org.coco.infra.spring.security

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.coco.core.type.BinaryId
import org.coco.core.utils.JsonUtils
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.vo.LegalName
import org.coco.domain.model.user.vo.Username
import org.coco.infra.spring.security.TokenProviderJwtHelper.Companion.CLAIM_ID
import org.coco.infra.spring.security.TokenProviderJwtHelper.Companion.key
import java.time.Duration

class JwtTokenProviderTest :
    FunSpec({
        val toPrincipal: DecodedJWT.() -> UserPrincipal = {
            UserPrincipal(
                id = BinaryId.fromString(getClaim(CLAIM_ID).asString()),
                roles =
                    JsonUtils
                        .deserialize(this.key("roles"), Set::class.java)
                        .map {
                            it.toString()
                        }.toSet(),
                username = Username(this.key("username")),
                legalName = LegalName(this.key("legalName")),
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
            val jwtString = authToken.value

            val decoded = JWT.decode(jwtString)
            val principal = decoded.toPrincipal()

            principal.id.value shouldBe id.value
            principal.username shouldBe username
            principal.roles shouldBe setOf("ROLE_USER")
        }
    })
