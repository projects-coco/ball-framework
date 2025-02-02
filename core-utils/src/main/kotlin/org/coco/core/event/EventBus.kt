package org.coco.core.event

import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers

object EventBus {
    private val eventSinks: Sinks.Many<Event> = Sinks.many().replay().latest()

    fun events(): Flux<Event> = eventSinks.asFlux()

    fun publish(event: Event): Sinks.EmitResult = eventSinks.tryEmitNext(event)

    inline fun <reified T : Event> receive(noinline action: (T) -> Unit) {
        events()
            .subscribeOn(Schedulers.boundedElastic())
            .filter { it is T }
            .map { it as T }
            .doOnNext(action)
            // TODO : Error 후처리
            .onErrorContinue({ _, _ -> })
            .subscribe()
    }
}
