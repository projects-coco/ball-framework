package org.coco.example.presentation

import org.coco.domain.model.user.BasicUserSearchDto
import org.coco.example.application.UserService
import org.coco.example.domain.model.user.UserRepository
import org.coco.infra.auth.jpa.EnableBallAuthJpaInfra
import org.coco.infra.jpa.EnableJpaConfig
import org.coco.presentation.mvc.core.EnableBallApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.domain.PageRequest
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
    private val userRepository: UserRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        userService.createUser("coco")
        userService.createUser("coco2")
        userService.updateUsername("coco", "ball")

        val searchResult = userRepository.search(BasicUserSearchDto(), PageRequest.of(0, 1))
        println(searchResult.content)
        println(searchResult.totalPages)
        println(searchResult.totalElements)
    }
}