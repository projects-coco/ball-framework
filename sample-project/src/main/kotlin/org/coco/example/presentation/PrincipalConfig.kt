package org.coco.example.presentation

import org.coco.core.type.BinaryId
import org.coco.core.utils.JsonUtils
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.vo.LegalName
import org.coco.domain.model.user.vo.Username
import org.coco.infra.spring.security.UserPrincipalBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PrincipalConfig {
    @Bean
    fun principalBuilder(): UserPrincipalBuilder =
        {
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
}
