package org.coco.example.presentation

import org.coco.application.DistributedLockAop
import org.coco.domain.model.auth.RefreshTokenRepository
import org.coco.domain.model.revision.BallRevisionDto
import org.coco.domain.model.user.BasicUser
import org.coco.example.application.UserService
import org.coco.example.domain.model.user.User
import org.coco.example.domain.model.user.UserRepository
import org.coco.infra.auth.redis.EnableBallAuthRedisInfra
import org.coco.infra.jpa.EnableJpaConfig
import org.coco.infra.redis.EnableRedisConfig
import org.coco.presentation.mvc.core.EnableBallApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component

@SpringBootApplication(
    scanBasePackages = ["org.coco.example"],
)
@EnableBallApplication
@EnableJpaConfig(
    entityBasePackages = [
        EnableJpaConfig.BALL_INFRA_ENTITY_PACKAGE,
        "org.coco.example.infra.jpa.model.*",
    ],
    repositoryBasePackages = [
        "org.coco.example.infra.jpa.model",
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
    DistributedLockAop::class,
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
    private val refreshTokenRepository: RefreshTokenRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        userService.createUser(BasicUser.Username("coco-user"), User.Password("coco-password"))
        val createdUser = userService.findUser(BasicUser.Username("coco-user"))
        userRepository.findRevisions(createdUser.id).forEach {
            println(BallRevisionDto.of(it))
        }
    }
}
