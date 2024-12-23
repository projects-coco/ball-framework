package org.coco.infra.jpa.model

import org.coco.core.type.BinaryId
import org.coco.domain.TestEntity
import org.coco.domain.TestEntityRepository
import org.coco.infra.jpa.helper.JpaRepositoryHelper
import org.springframework.stereotype.Repository

@Repository
class TestEntityRepositoryImpl(
    private val jpaRepository: TestEntityJpaRepository,
) : JpaRepositoryHelper<TestEntity, TestEntityDataModel>(jpaRepository, TestEntity::class),
    TestEntityRepository {
    override fun TestEntityDataModel.toEntity(): TestEntity =
        TestEntity(
            id = BinaryId(id),
            createdAt = createdAt,
            updatedAt = updatedAt,
            payload = payload,
        )

    override fun save(entity: TestEntity): TestEntity {
        val dataModel = TestEntityDataModel.of(entity)
        return jpaRepository.save(dataModel).toEntity()
    }
}
