package org.coco.infra.kafka

import org.coco.infra.kafka.event.KafkaEventConsumer
import org.coco.infra.kafka.event.KafkaEventPublisher
import org.springframework.context.annotation.Import

@Import(
    KafkaConfig::class,
    KafkaEventPublisher::class,
    KafkaEventConsumer::class,
)
annotation class EnableKafkaConfig
