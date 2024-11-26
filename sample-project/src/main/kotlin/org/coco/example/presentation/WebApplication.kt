package org.coco.example.presentation

import org.coco.infra.jpa.EnableJpaConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableJpaConfig(
    entityBasePackages = [EnableJpaConfig.BALL_INFRA_ENTITY_PACKAGE, "org.coco.example.domain.model.*"],
    repositoryBasePackages = ["org.coco.example.domain.repository"]
)
class WebApplication

fun main(args: Array<String>) {
    val application = SpringApplication(WebApplication::class.java)
    application.run(*args)
}
