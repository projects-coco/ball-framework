package org.coco.example.presentation

import arrow.core.raise.fold
import org.coco.domain.utils.logger
import org.coco.example.application.UserService
import org.coco.infra.jpa.EnableJpaConfig
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@SpringBootApplication(
    scanBasePackages = ["org.coco.example"]
)
@EnableJpaConfig(
    entityBasePackages = [EnableJpaConfig.BALL_INFRA_ENTITY_PACKAGE, "org.coco.example.domain.model.*"],
    repositoryBasePackages = ["org.coco.example.infra.jpa"]
)
class WebApplication

fun main(args: Array<String>) {
    val application = SpringApplication(WebApplication::class.java)
    application.run(*args)
}

@Component
class SampleCommandLineRunner(
    private val userService: UserService,
) : CommandLineRunner {
    val logger = logger()

    @Transactional
    override fun run(vararg args: String?) {
        userService.createUser("coco")
            .fold({
                when (it) {
                    is UserService.CreateUserError.AlreadyExist -> logger.error("User ${it.username} already exist")
                    UserService.CreateUserError.CrudError -> logger.error("Create user error")
                }
            }, {
                logger.info("Create user ${it.username} success")
            })
        userService.updateUsername("coco", "ball")
            .fold({
                when (it) {
                    UserService.UpdateUserError.NotFound -> logger.error("User not found")
                    UserService.UpdateUserError.CrudError -> logger.error("Update user error")
                }
            }, {
                logger.info("Update user success")
            })
    }
}