package org.coco.infra.jpa.model

import org.coco.infra.jpa.helper.JpaRepositoryHelper
import org.coco.infra.jpa.model.domain.TestEntity
import org.coco.infra.jpa.model.domain.TestRepository
import org.coco.infra.jpa.model.infra.TestDataModel
import org.coco.infra.jpa.model.infra.TestEntityJpaRepository
import org.springframework.stereotype.Repository

@Repository
class TestRepositoryImpl(
    private val jpaRepository: TestEntityJpaRepository,
) : JpaRepositoryHelper<TestEntity, TestDataModel>(jpaRepository, TestEntity::class),
    TestRepository {
    override fun save(entity: TestEntity): TestEntity {
        val dataModel = TestDataModel.of(entity)
        return jpaRepository.save(dataModel).toEntity()
    }
}
