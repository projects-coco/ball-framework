package org.coco.infra.mongodb

import io.kotest.matchers.shouldBe
import org.coco.core.type.BinaryId
import org.coco.domain.TestEntity
import org.coco.domain.TestEntityRepository
import org.coco.infra.mongodb.model.TestEntityMongoRepository
import org.coco.infra.mongodb.model.TestEntityRepositoryImpl
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

@ContextConfiguration(
    classes = [TestEntityRepositoryImpl::class],
)
@EnableMongodbConfig(
    basePackages = ["org.coco.infra.mongodb.model"],
)
class MongoRepositoryTest(
    private val testEntityRepository: TestEntityRepository,
    private val mongoRepository: TestEntityMongoRepository,
) : MongodbTestSpec() {
    init {
        this.test("TestEntityRepository.save()") {
            // given
            val testEntity = TestEntity(BinaryId.new(), LocalDateTime.now(), LocalDateTime.now(), "test")

            // when
            testEntityRepository.save(testEntity)

            // then
            testEntityRepository.findById(testEntity.id).get().payload shouldBe "test"
        }

        this.test("TestEntityRepository.update()") {
            // given
            val testEntity = TestEntity(BinaryId.new(), LocalDateTime.now(), LocalDateTime.now(), "test")
            testEntityRepository.save(testEntity)
            val documentCount = mongoRepository.count()

            // when
            testEntityRepository.update(testEntity.id) {
                it.payload = "updated"
            }

            // then
            mongoRepository.count() shouldBe documentCount
            testEntityRepository.findById(testEntity.id).get().payload shouldBe "updated"
        }
    }
}
