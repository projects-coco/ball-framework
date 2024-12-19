package org.coco.infra.mongodb.model

import org.coco.domain.TestEntity
import org.coco.domain.TestEntityRepository
import org.coco.infra.mongodb.helper.MongoRepositoryHelper
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class TestEntityRepositoryImpl(
    private val mongoRepository: MongoRepository<TestEntityDocumentModel, String>,
) : MongoRepositoryHelper<TestEntity, TestEntityDocumentModel>(mongoRepository, TestEntity::class),
    TestEntityRepository {
    override fun save(entity: TestEntity): TestEntity {
        val documentModel = TestEntityDocumentModel.of(entity)
        return mongoRepository.save(documentModel).toEntity()
    }
}
