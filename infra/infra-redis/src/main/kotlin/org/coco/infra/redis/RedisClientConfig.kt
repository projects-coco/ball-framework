package org.coco.infra.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
class RedisClientConfig {
    @Bean
    fun redisConnectionFactory(env: Environment): LettuceConnectionFactory {
        val redisHost = env.getProperty("app.redis.host") ?: "localhost"
        val redisPort = (env.getProperty("app.redis.port") ?: "6379").toInt()
        return LettuceConnectionFactory(
            RedisStandaloneConfiguration(
                redisHost,
                redisPort,
            ),
        )
    }
}
