package org.coco.core.event

import org.coco.core.utils.currentTimestamp
import kotlin.reflect.KClass

abstract class Event(
    open val key: String = "",
    open val timestamp: Long = currentTimestamp(),
    open val source: String = "",
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
