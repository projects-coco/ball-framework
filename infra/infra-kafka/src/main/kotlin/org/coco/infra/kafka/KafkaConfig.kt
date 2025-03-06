package org.coco.infra.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.*

@Configuration
class KafkaConfig {
    @Bean
    fun kafkaConsumer(env: Environment): KafkaConsumer<String, String> {
        val bootstrapServers = env.getRequiredProperty("spring.kafka.consumer.bootstrap-servers")
        val groupId = env.getRequiredProperty("spring.kafka.consumer.group-id")
        val autoOffsetReset = env.getRequiredProperty("spring.kafka.consumer.auto-offset-reset")
        val keyDeserializer =
            env.getProperty("spring.kafka.consumer.key-deserializer")
                ?: "org.apache.kafka.common.serialization.StringDeserializer"
        val valueDeserializer =
            env.getProperty("spring.kafka.consumer.value-deserializer")
                ?: "org.apache.kafka.common.serialization.StringDeserializer"

        val properties = Properties()
        properties["bootstrap.servers"] = bootstrapServers
        properties["group.id"] = groupId
        properties["auto.offset.reset"] = autoOffsetReset
        properties["key.deserializer"] = keyDeserializer
        properties["value.deserializer"] = valueDeserializer

        val consumer = KafkaConsumer<String, String>(properties)
        return consumer
    }

    @Bean
    fun kafkaTaskExecutor(): TaskExecutor =
        ThreadPoolTaskExecutor().apply {
            corePoolSize = 1
            maxPoolSize = 1
            queueCapacity = 1
        }
}
