package org.coco.infra.mongodb

import org.springframework.core.annotation.AliasFor
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoAuditing
@EnableMongoRepositories
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnableMongodbConfig(
    @get:AliasFor(
        annotation = EnableMongoRepositories::class,
        attribute = "basePackages",
    )
    val basePackages: Array<String> = [],
)
