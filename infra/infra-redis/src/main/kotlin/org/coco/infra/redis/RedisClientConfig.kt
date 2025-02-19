package org.coco.infra.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class RedisClientConfig(
    env: Environment,
) {
    val redisHost = env.getProperty("app.redis.host") ?: "localhost"
    val redisPort = (env.getProperty("app.redis.port") ?: "6379").toInt()
    val redisPassword: String? = env.getProperty("app.redis.password")

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config
            .useSingleServer()
            .setAddress("redis://$redisHost:$redisPort")
            .apply {
                if (redisPassword != null) password = redisPassword
            }.setConnectionPoolSize(30)
        return Redisson.create(config)
    }
}
