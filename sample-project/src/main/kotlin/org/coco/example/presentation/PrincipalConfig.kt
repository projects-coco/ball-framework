package org.coco.example.presentation

import org.coco.core.type.BinaryId
import org.coco.core.utils.JsonUtils
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.BasicUser
import org.coco.example.domain.model.user.User
import org.coco.infra.spring.security.JwtTokenProvider.Companion.CLAIM_ID
import org.coco.infra.spring.security.JwtTokenProvider.Companion.key
import org.coco.infra.spring.security.PrincipalBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PrincipalConfig {
    @Bean
    fun principalBuilder(): PrincipalBuilder {
        return {
            UserPrincipal(
                id = BinaryId.fromString(getClaim(CLAIM_ID).asString()),
                roles = JsonUtils.deserialize(this.key("roles"), Set::class.java)
                    .map {
                        User.Role.valueOf(it as String)
                    }
                    .toSet(),
                username = BasicUser.Username(this.key("username")),
            )
        }
    }
}