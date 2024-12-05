package org.coco.example.presentation

import org.coco.example.application.UserService
import org.coco.infra.auth.jpa.EnableBallAuthJpaInfra
import org.coco.infra.jpa.EnableJpaConfig
import org.coco.presentation.mvc.core.EnableBallApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Component

@SpringBootApplication(
    scanBasePackages = ["org.coco.example"]
)
@EnableBallApplication
@EnableJpaConfig(
    entityBasePackages = [
        EnableJpaConfig.BALL_INFRA_ENTITY_PACKAGE,
        EnableBallAuthJpaInfra.BALL_AUTH_JPA_ENTITY_PACKAGE,
        "org.coco.example.infra.jpa.model.*"
    ],
    repositoryBasePackages = [
        EnableBallAuthJpaInfra.BALL_AUTH_JPA_REPOSITORY_PACKAGE,
        "org.coco.example.infra.jpa.model"
    ]
)
@EnableBallAuthJpaInfra
class WebApplication

fun main(args: Array<String>) {
    val application = SpringApplication(WebApplication::class.java)
    application.run(*args)
}

@Component
class SampleCommandLineRunner(
    private val userService: UserService,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        userService.createUser("coco")
        userService.createUser("coco2")
        userService.updateUsername("coco", "ball")
    }
}