package org.coco.infra.kafka.event

import org.apache.kafka.clients.producer.ProducerRecord
import org.coco.core.event.Event
import org.coco.core.event.EventPublisher
import org.coco.core.utils.JsonUtils
import org.springframework.context.annotation.Primary
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
@Primary
class KafkaEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : EventPublisher {
    override fun publish(event: Event) {
        kafkaTemplate.send(ProducerRecord(event::class.simpleName, JsonUtils.serialize(event)))
    }
}
