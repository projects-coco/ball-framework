package org.coco.infra.redis

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.AliasFor
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@EntityScan
@EnableRedisRepositories
@Import(
    RedisClientConfig::class,
)
annotation class EnableRedisConfig(
    @get:AliasFor(
        annotation = EntityScan::class,
        attribute = "basePackages",
    ) val entityBasePackages: Array<String> = [],
    @get:AliasFor(
        annotation = EnableRedisRepositories::class,
        attribute = "basePackages",
    )
    val repositoryBasePackages: Array<String> = [],
)
