package org.coco.core.event

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import org.coco.core.utils.currentTimestamp

class EventBusTest :
    FunSpec({
        test("EventBus#publish(Event)") {
            // given
            var triggered = false
            val timestamp = currentTimestamp()

            data class TestEvent(
                override val timestamp: Long,
                val name: String,
            ) : Event()
            val testEvent = TestEvent(timestamp, "coco")

            // when
            EventBus.receive<TestEvent> {
                triggered = true
                it shouldBe testEvent
                it.timestamp shouldBe timestamp
                it.name shouldBe "coco"
            }
            EventBus.publish(testEvent)

            // then
            delay(500)
            triggered shouldBe true
        }
    })
