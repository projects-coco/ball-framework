package org.coco.infra.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class RedisClientConfig {
    @Bean
    fun redissonConnectionFactory(redisson: RedissonClient): RedissonConnectionFactory = RedissonConnectionFactory(redisson)

    @Bean(destroyMethod = "shutdown")
    fun redisson(env: Environment): RedissonClient {
        val redisHost = env.getRequiredProperty("spring.data.redis.host")
        val redisPort = env.getRequiredProperty("spring.data.redis.port", Int::class.java)
        val redisPassword: String? = env.getProperty("spring.data.redis.password")

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
