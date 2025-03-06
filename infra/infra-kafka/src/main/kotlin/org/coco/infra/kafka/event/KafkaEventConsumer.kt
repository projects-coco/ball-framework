package org.coco.infra.kafka.event

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.coco.core.event.Event
import org.coco.core.event.EventConsumer
import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.core.utils.JsonUtils
import org.coco.core.utils.logger
import org.springframework.context.annotation.Primary
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.time.Duration
import kotlin.reflect.KClass

@Component
@Primary
class KafkaEventConsumer(
    private val kafkaConsumer: KafkaConsumer<String, String>,
    private val taskExecutor: TaskExecutor,
    platformTransactionManager: PlatformTransactionManager,
) : EventConsumer {
    private val consumers = mutableMapOf<KClass<*>, ArrayList<(Event) -> Unit>>()
    private val transactionTemplate = TransactionTemplate(platformTransactionManager)

    @Volatile
    private var running = true

    @PostConstruct
    fun postConstruct() {
        taskExecutor.execute { startPolling() }
    }

    override fun <T : Event> consume(
        type: KClass<T>,
        action: (T) -> Unit,
    ) {
        if (!consumers.containsKey(type)) {
            consumers[type] = ArrayList()
        }
        @Suppress("UNCHECKED_CAST")
        consumers[type]!!.add(action as (Event) -> Unit)
        updateSubscribers()
    }

    private fun updateSubscribers() {
        kafkaConsumer.subscribe(consumers.keys.map { it.simpleName })
    }

    fun startPolling() {
        try {
            while (running) {
                val records = kafkaConsumer.poll(Duration.ofMillis(1000))
                for (record in records) {
                    val topic = record.topic()
                    val kClass = consumers.keys.find { it.simpleName == topic } ?: continue
                    val consumers = consumers[kClass] ?: continue
                    val event = JsonUtils.deserialize(record.value(), kClass) as Event

                    taskExecutor.execute {
                        consumers.forEach { action ->
                            try {
                                transactionTemplate.execute {
                                    action(event)
                                } ?: throw LogicError("트랜잭션 처리에 실패했습니다.", ErrorType.INTERNAL_SERVER_ERROR)
                            } catch (e: Exception) {
                                logger().error("Error on consuming event: {}", event, e)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            logger().error("Error in KafkaConsumer", e)
        } finally {
            kafkaConsumer.close()
        }
    }

    @PreDestroy
    fun preDestroy() {
        running = false
        kafkaConsumer.wakeup()
    }
}
