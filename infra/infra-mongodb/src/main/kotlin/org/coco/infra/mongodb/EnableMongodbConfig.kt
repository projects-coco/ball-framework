package org.coco.infra.mongodb

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.core.annotation.AliasFor
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoAuditing
@EntityScan
@EnableMongoRepositories
annotation class EnableMongodbConfig(
    @get:AliasFor(
        annotation = EntityScan::class, attribute = "basePackages"
    ) val entityBasePackages: Array<String> = [BALL_INFRA_PACKAGE],

    @get:AliasFor(
        annotation = EnableMongoRepositories::class, attribute = "basePackages"
    )
    val repositoryBasePackages: Array<String> = [],
) {
    companion object {
        const val BALL_INFRA_PACKAGE = "org.coco.infra.mongodb"
    }
}