package org.coco.infra.kafka.event

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import org.coco.core.event.Event
import org.coco.core.event.EventConsumer
import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.core.utils.JsonUtils
import org.coco.core.utils.logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

@Component
@Primary
class KafkaEventConsumer(
    private val kafkaConsumer: KafkaConsumer<String, String>,
    @Qualifier("kafkaTaskExecutor") private val taskExecutor: TaskExecutor,
    platformTransactionManager: PlatformTransactionManager,
) : EventConsumer {
    private val consumers: ConcurrentHashMap<KClass<*>, ArrayList<(Event) -> Unit>> = ConcurrentHashMap()
    private val transactionTemplate = TransactionTemplate(platformTransactionManager)

    @Volatile
    private var running = true

    @Volatile
    private var subscriptionChanged = false

    @PostConstruct
    fun postConstruct() {
        taskExecutor.execute { startPolling() }
    }

    @PreDestroy
    fun preDestroy() {
        running = false
        kafkaConsumer.wakeup()
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
        subscriptionChanged = true
    }

    fun startPolling() {
        try {
            while (running) {
                updateSubscriptionIfNeeded()
                if (kafkaConsumer.subscription().isEmpty()) continue
                val records = kafkaConsumer.poll(Duration.ofMillis(1000))
                for (record in records) {
                    processRecord(record)
                }
            }
        } catch (e: WakeupException) {
            if (running) {
                throw e
            }
        } catch (e: Exception) {
            logger().error("Error in KafkaConsumer", e)
        } finally {
            kafkaConsumer.close()
        }
    }

    private fun updateSubscriptionIfNeeded() {
        if (subscriptionChanged) {
            val topics = consumers.keys.map { it.topic() }.toSet()
            if (kafkaConsumer.subscription() != topics) {
                kafkaConsumer.subscribe(topics)
            }
            subscriptionChanged = false
        }
    }

    private fun processRecord(record: ConsumerRecord<String, String>) {
        val topic = record.topic()
        val kClass = consumers.keys.find { it.topic() == topic } ?: return
        val eventConsumers = consumers[kClass] ?: return
        val event = JsonUtils.deserialize(record.value(), kClass) as Event
        eventConsumers.forEach { action ->
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
