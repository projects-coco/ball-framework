package org.coco.infra.mongodb.model

import org.coco.core.type.BinaryId
import org.coco.domain.TestEntity
import org.coco.domain.TestEntityRepository
import org.coco.infra.mongodb.helper.CustomMongoRepository
import org.coco.infra.mongodb.helper.MongoRepositoryHelper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class TestEntityRepositoryImpl(
    mongoRepository: CustomMongoRepository<TestEntityDocumentModel>,
) : MongoRepositoryHelper<TestEntity, TestEntityDocumentModel>(mongoRepository, TestEntity::class),
    TestEntityRepository {
    override fun TestEntityDocumentModel.toEntity(): TestEntity =
        TestEntity(
            id = BinaryId.fromString(entityId),
            createdAt = createdAt,
            updatedAt = updatedAt,
            payload = payload,
        )

    override fun TestEntity.toModel(): TestEntityDocumentModel = TestEntityDocumentModel.of(this)
}
