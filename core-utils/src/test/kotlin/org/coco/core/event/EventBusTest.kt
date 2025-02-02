package org.coco.core.event

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import org.coco.core.utils.currentTimestamp

class EventBusTest :
    FunSpec({
        data class TestEvent(
            override val timestamp: Long,
            val name: String,
        ) : Event()

        test("EventBus#publish(Event)") {
            // given
            var triggered = false
            val timestamp = currentTimestamp()

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

        test("이벤트 버스 후처리 로직이 예외를 던지더라도, 다른 이벤트는 계속 후처리 가능해야함") {
            data class AnotherEvent(
                override val timestamp: Long,
                val name: String,
            ) : Event()

            val timestamp = currentTimestamp()

            EventBus.receive<TestEvent> {
                throw Exception()
            }
            EventBus.publish(TestEvent(timestamp, "coco"))
            delay(500)

            var triggered = false
            // when
            EventBus.receive<AnotherEvent> {
                triggered = true
            }
            EventBus.publish(TestEvent(timestamp, "coco"))

            delay(1000)

            triggered shouldBe true
        }
    })
