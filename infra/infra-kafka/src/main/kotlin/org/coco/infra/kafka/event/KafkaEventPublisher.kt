package org.coco.infra.kafka.event

import org.coco.core.event.Event
import org.coco.core.event.EventPublisher
import org.coco.core.utils.JsonUtils
import org.springframework.context.annotation.Primary
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
@Primary
class KafkaEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : EventPublisher {
    override fun publish(event: Event) {
        kafkaTemplate.send(event.topic(), JsonUtils.serialize(event))
    }
}
