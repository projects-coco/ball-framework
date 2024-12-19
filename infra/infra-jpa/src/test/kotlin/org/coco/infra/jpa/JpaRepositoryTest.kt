package org.coco.infra.jpa

import io.kotest.matchers.shouldBe
import org.coco.core.type.BinaryId
import org.coco.infra.jpa.model.TestRepositoryImpl
import org.coco.infra.jpa.model.domain.TestEntity
import org.coco.infra.jpa.model.domain.TestRepository
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

@ContextConfiguration(
    classes = [
        TestRepositoryImpl::class,
    ],
)
@EnableJpaConfig(
    entityBasePackages = ["org.coco.infra.jpa"],
    repositoryBasePackages = ["org.coco.infra.jpa"],
)
class JpaRepositoryTest(
    private val testRepository: TestRepository,
) : JpaTestSpecUsingMariadb() {
    init {
        this.test("TestRepository.save()") {
            // given
            val testEntity = TestEntity(BinaryId.new(), LocalDateTime.now(), LocalDateTime.now(), "test")

            // when
            testRepository.save(testEntity)

            // then
            testRepository.findById(testEntity.id).get().payload shouldBe "test"
        }
    }
}
