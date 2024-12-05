package org.coco.infra.spring.security

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.coco.core.utils.JsonUtils
import org.coco.domain.model.BinaryId
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.BasicUser
import org.coco.infra.spring.security.JwtTokenProvider.Companion.CLAIM_ID
import org.coco.infra.spring.security.JwtTokenProvider.Companion.key
import java.time.Duration

class JwtTokenProviderTest : FunSpec({
    val toPrincipal: DecodedJWT.() -> UserPrincipal = {
        UserPrincipal(
            id = BinaryId.fromString(getClaim(CLAIM_ID).asString()),
            roles = JsonUtils.deserialize(this.key("roles"), Set::class.java)
                .map {
                    BasicUser.Role.valueOf(it as String)
                }
                .toSet(),
            username = BasicUser.Username(this.key("username")),
            actualName = BasicUser.Name(this.key("actualName"))
        )
    }
    val tokenProvider =
        JwtTokenProvider(
            issuer = "authProperties.issuer",
            secret = "authProperties.accessTokenSecret",
            expiry = Duration.ofMinutes(30),
            toPrincipal = toPrincipal,
        )

    test("DecodedJWT::payload(key: String)") {
        val id = BinaryId.new()
        val username = BasicUser.Username("username")
        val actualName = BasicUser.Name("actualName")
        val authToken = tokenProvider.generateToken(
            UserPrincipal(
                id = id,
                roles = setOf(BasicUser.Role.USER),
                username = username,
                actualName = actualName
            )
        )
        val jwtString = authToken.value

        val decoded = JWT.decode(jwtString)
        val principal = decoded.toPrincipal()

        principal.id.value shouldBe id.value
        principal.username shouldBe username
        principal.actualName shouldBe actualName
        principal.roles shouldBe setOf(BasicUser.Role.USER)
    }
})
