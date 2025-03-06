package org.coco.infra.kafka

import org.coco.infra.kafka.event.KafkaEventConsumer
import org.coco.infra.kafka.event.KafkaEventProducer
import org.springframework.context.annotation.Import

@Import(
    KafkaEventProducer::class,
    KafkaEventConsumer::class,
)
annotation class EnableKafkaConfig
