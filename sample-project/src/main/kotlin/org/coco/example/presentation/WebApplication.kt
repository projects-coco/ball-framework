package org.coco.example.presentation

import org.coco.application.lock.DistributedLockAspect
import org.coco.core.event.Event
import org.coco.core.event.EventConsumer
import org.coco.core.event.EventPublisher
import org.coco.core.type.BinaryId
import org.coco.core.utils.logger
import org.coco.example.application.UserService
import org.coco.example.domain.model.memo.Memo
import org.coco.example.domain.model.memo.MemoRepository
import org.coco.example.domain.model.user.UserRepository
import org.coco.infra.auth.redis.EnableBallAuthRedisInfra
import org.coco.infra.jpa.EnableJpaConfig
import org.coco.infra.kafka.EnableKafkaConfig
import org.coco.infra.redis.EnableRedisConfig
import org.coco.presentation.mvc.core.EnableBallWebMvc
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.stereotype.Component

@SpringBootApplication(
    scanBasePackages = ["org.coco.example"],
)
@EnableBallWebMvc
@EnableJpaConfig(
    entityBasePackages = [
        EnableJpaConfig.BALL_INFRA_ENTITY_PACKAGE,
        "org.coco.example.infra.jpa.model.*",
    ],
    repositoryBasePackages = [
        "org.coco.example.infra.jpa.model",
    ],
)
@EnableMongoRepositories(
    basePackages = [
        "org.coco.example.infra.mongodb.*",
    ],
)
@EnableRedisConfig(
    entityBasePackages = [
        EnableBallAuthRedisInfra.REDIS_ENTITY_PACKAGE,
    ],
    repositoryBasePackages = [
        EnableBallAuthRedisInfra.REDIS_REPOSITORY_PACKAGE,
    ],
)
@EnableBallAuthRedisInfra
@EnableKafkaConfig
@Import(
    DistributedLockAspect::class,
)
class WebApplication

fun main(args: Array<String>) {
    val application = SpringApplication(WebApplication::class.java)
    application.run(*args)
}

@Component
class SampleCommandLineRunner(
    private val eventPublisher: EventPublisher,
    private val eventConsumer: EventConsumer,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val memoRepository: MemoRepository,
) : CommandLineRunner {
    data class SampleEvent(
        val message: String,
    ) : Event()

    override fun run(vararg args: String?) {
        eventPublisher.publish(SampleEvent("Hello World!"))
    }

    init {
        eventConsumer.consume(SampleEvent::class) {
            logger().info("Received event: $it")
            val memo =
                Memo(
                    targetId = BinaryId.new(),
                    writerId = BinaryId.new(),
                    content = "Hello world!",
                )
            memoRepository.save(memo)
            val createdMemo = memoRepository.findById(memo.id)
            println(createdMemo.get())
        }
    }
}
