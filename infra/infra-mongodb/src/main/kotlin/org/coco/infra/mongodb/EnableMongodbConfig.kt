package org.coco.infra.mongodb

import org.springframework.core.annotation.AliasFor
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoAuditing
@EnableMongoRepositories
annotation class EnableMongodbConfig(
    @get:AliasFor(
        annotation = EnableMongoRepositories::class,
        attribute = "basePackages",
    )
    val repositoryBasePackages: Array<String> = [],
) {
    companion object {
        const val BALL_INFRA_PACKAGE = "org.coco.infra.mongodb"
    }
}
