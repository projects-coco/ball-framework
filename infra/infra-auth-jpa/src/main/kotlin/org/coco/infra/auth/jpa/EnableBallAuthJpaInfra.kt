package org.coco.infra.auth.jpa

import org.coco.infra.spring.security.Argon2HashProvider
import org.springframework.context.annotation.Import

@Import(
    Argon2HashProvider::class,
    RefreshTokenRepositoryImpl::class,
)
annotation class EnableBallAuthJpaInfra {
    companion object {
        const val BALL_AUTH_JPA_ENTITY_PACKAGE = "org.coco.infra.auth.jpa.model"
        const val BALL_AUTH_JPA_REPOSITORY_PACKAGE = "org.coco.infra.auth.jpa.model"
    }
}
