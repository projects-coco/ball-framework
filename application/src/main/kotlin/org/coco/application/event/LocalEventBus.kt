package org.coco.application.event

import org.coco.core.event.Event
import org.coco.core.event.EventConsumer
import org.coco.core.event.EventPublisher
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers
import kotlin.reflect.KClass

@Order(Ordered.LOWEST_PRECEDENCE)
class LocalEventBus : EventPublisher, EventConsumer {
    private val eventSinks: Sinks.Many<Event> = Sinks.many().replay().latest()

    fun events(): Flux<Event> = eventSinks.asFlux()

    override fun publish(event: Event) {
        eventSinks.tryEmitNext(event)
    }

    override fun <T : Event> consume(type: KClass<T>, action: (T) -> Unit) {
        events()
            .subscribeOn(Schedulers.boundedElastic())
            .filter { type.isInstance(it) }
            .map { type.javaObjectType.cast(it) }
            .doOnNext(action)
            // TODO : Error 후처리
            .onErrorContinue({ _, _ -> })
            .subscribe()
    }
}
