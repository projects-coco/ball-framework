package org.coco.presentation.mvc.config

import org.coco.domain.service.auth.TokenProvider
import org.coco.infra.spring.security.JwtTokenProvider
import org.coco.infra.spring.security.PrincipalBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@EnableConfigurationProperties(JwtConfig.AuthProperties::class)
class JwtConfig(
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private val principalBuilder: PrincipalBuilder,
) {
    @ConfigurationProperties(prefix = "app.auth")
    class AuthProperties(
        val issuer: String,
        val accessTokenSecret: String,
        val refreshTokenSecret: String,
    )

    @Bean("accessTokenProvider")
    fun accessTokenProvider(authProperties: AuthProperties): TokenProvider =
        JwtTokenProvider(
            secret = authProperties.accessTokenSecret,
            issuer = authProperties.issuer,
            expiry = Duration.ofMinutes(30),
            toPrincipal = principalBuilder
        )

    @Bean("refreshTokenProvider")
    fun refreshTokenProvider(authProperties: AuthProperties): TokenProvider =
        JwtTokenProvider(
            secret = authProperties.refreshTokenSecret,
            issuer = authProperties.issuer,
            expiry = Duration.ofDays(14),
            toPrincipal = principalBuilder
        )
}