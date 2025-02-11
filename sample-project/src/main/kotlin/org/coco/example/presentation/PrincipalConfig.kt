package org.coco.example.presentation

import org.coco.core.type.BinaryId
import org.coco.core.utils.JsonUtils
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.vo.LegalName
import org.coco.domain.model.user.vo.Username
import org.coco.example.domain.model.user.User
import org.coco.infra.spring.security.TokenProviderJwtHelper.Companion.CLAIM_ID
import org.coco.infra.spring.security.TokenProviderJwtHelper.Companion.key
import org.coco.infra.spring.security.UserPrincipalBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PrincipalConfig {
    @Bean
    fun principalBuilder(): UserPrincipalBuilder =
        {
            UserPrincipal(
                id = BinaryId.fromString(getClaim(CLAIM_ID).asString()),
                roles =
                    JsonUtils
                        .deserialize(this.key("roles"), Set::class.java)
                        .map {
                            User.Role.valueOf(it as String).name
                        }.toSet(),
                username = Username(this.key("username")),
                legalName = LegalName(this.key("legalName")),
            )
        }
}
