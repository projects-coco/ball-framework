package org.coco.infra.auth.redis

import org.coco.infra.spring.security.Argon2HashProvider
import org.springframework.context.annotation.Import

@Import(
    Argon2HashProvider::class,
    RefreshTokenRepositoryImpl::class,
)
annotation class EnableBallAuthRedisInfra {
    companion object {
        const val REDIS_ENTITY_PACKAGE = "org.coco.infra.auth.redis.model"
        const val REDIS_REPOSITORY_PACKAGE = "org.coco.infra.auth.redis.model"
    }
}
