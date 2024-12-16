package org.coco.infra.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
class RedisClientConfig(
    env: Environment
) {
    val redisHost = env.getProperty("app.redis.host") ?: "localhost"
    val redisPort = (env.getProperty("app.redis.port") ?: "6379").toInt()
    val redisPassword = env.getProperty("app.redis.password") ?: ""

    @Bean
    fun redisLettuceConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory(
            RedisStandaloneConfiguration(
                redisHost,
                redisPort,
            ),
        )
    }

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer()
            .setAddress("redis://${redisHost}:${redisPort}")
            .setConnectionPoolSize(30)
        return Redisson.create(config)
    }
}
