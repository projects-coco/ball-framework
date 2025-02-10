package org.coco.example.presentation

import arrow.core.toOption
import org.coco.application.lock.DistributedLockAspect
import org.coco.domain.model.revision.BallRevisionDto
import org.coco.domain.model.user.BasicUserSearchDto
import org.coco.domain.model.user.vo.Password
import org.coco.domain.model.user.vo.Username
import org.coco.example.application.UserService
import org.coco.example.domain.model.memo.Memo
import org.coco.example.domain.model.memo.MemoRepository
import org.coco.example.domain.model.user.UserRepository
import org.coco.infra.auth.redis.EnableBallAuthRedisInfra
import org.coco.infra.jpa.EnableJpaConfig
import org.coco.infra.redis.EnableRedisConfig
import org.coco.presentation.mvc.core.EnableBallWebMvc
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val memoRepository: MemoRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        userService.createUser(Username("coco-user"), Password("coco-password"))
        userService.createUser(Username("coco-user-1"), Password("coco-password"))
        userService.createUser(Username("coco-user-2"), Password("coco-password"))
        userService.createUser(Username("coco-user-3"), Password("coco-password"))
        userService.createUser(Username("coco-user-4"), Password("coco-password"))

        val createdUser = userService.findUser(Username("coco-user"))
        userRepository.findRevisions(createdUser.id).forEach {
            println(BallRevisionDto.of(it))
        }

        userRepository
            .search(
                BasicUserSearchDto(
                    username = "coco-user-".toOption(),
                ),
                pageable =
                    PageRequest.of(0, 10).withSort(
                        Sort.by("username").descending(),
                    ),
            ).forEach {
                println(it)
            }

        val memo =
            Memo(
                targetId = createdUser.id,
                writer = createdUser,
                content = "Hello world!",
            )
        memoRepository.save(memo)
        val createdMemo = memoRepository.findById(memo.id)
        println(createdMemo.get())
    }
}
