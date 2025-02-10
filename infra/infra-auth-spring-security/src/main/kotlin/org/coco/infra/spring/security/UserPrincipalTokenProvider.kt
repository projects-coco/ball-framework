package org.coco.infra.spring.security

import com.auth0.jwt.interfaces.DecodedJWT
import org.coco.core.utils.JsonUtils
import org.coco.domain.model.auth.UserPrincipal
import java.time.Duration

typealias UserPrincipalBuilder = DecodedJWT.() -> UserPrincipal

class UserPrincipalTokenProvider(
    secret: String,
    issuer: String,
    expiry: Duration,
    toPrincipal: UserPrincipalBuilder,
) : TokenProviderJwtHelper<UserPrincipal>(secret, issuer, expiry, toPrincipal) {
    override fun generatePayload(principal: UserPrincipal): Map<String, String> =
        mapOf(
            "roles" to JsonUtils.serialize(principal.roles),
            "username" to principal.username.value,
        )
}
