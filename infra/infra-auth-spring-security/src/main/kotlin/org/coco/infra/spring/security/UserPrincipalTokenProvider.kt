package org.coco.infra.spring.security

import io.jsonwebtoken.Claims
import org.coco.core.utils.JsonUtils
import org.coco.core.utils.currentClock
import org.coco.domain.model.auth.UserPrincipal
import java.time.Clock
import java.time.Duration

typealias UserPrincipalBuilder = Claims.() -> UserPrincipal

class UserPrincipalTokenProvider(
    secret: String,
    issuer: String,
    expiry: Duration,
    toPrincipal: Claims.() -> UserPrincipal,
    clock: Clock = currentClock()
) : TokenProviderJwtHelper<UserPrincipal>(secret, issuer, expiry, toPrincipal, clock) {
    override fun generatePayload(principal: UserPrincipal): Map<String, String> =
        mapOf(
            "roles" to JsonUtils.serialize(principal.roles),
            "username" to principal.username.value,
            "legalName" to principal.legalName.value,
        )
}
