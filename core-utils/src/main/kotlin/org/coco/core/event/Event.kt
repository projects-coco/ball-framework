package org.coco.core.event

import org.coco.core.utils.currentTimestamp
import kotlin.reflect.KClass

abstract class Event(
    open val timestamp: Long = currentTimestamp(),
)

interface EventPublisher {
    fun publish(event: Event)
}

interface EventConsumer {
    fun <T : Event> consume(
        type: KClass<T>,
        action: (T) -> Unit,
    )
}
