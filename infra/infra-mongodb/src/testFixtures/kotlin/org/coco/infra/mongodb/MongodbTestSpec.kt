package org.coco.infra.mongodb

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.coco.infra.mongodb.testcontainers.EnableMongodbContainer
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest

@DataMongoTest
@EnableMongodbContainer
@EnableAutoConfiguration
abstract class MongodbTestSpec(
    body: FunSpec.() -> Unit = {},
) : FunSpec(body) {
    override fun extensions() = listOf(SpringExtension)
}
