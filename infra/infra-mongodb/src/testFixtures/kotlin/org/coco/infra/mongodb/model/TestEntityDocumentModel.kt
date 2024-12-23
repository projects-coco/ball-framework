package org.coco.infra.mongodb.model

import org.coco.core.type.BinaryId
import org.coco.domain.TestEntity
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "test_entity")
class TestEntityDocumentModel(
    entityId: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    payload: String,
) : DocumentModel<TestEntity>(entityId, createdAt, updatedAt) {
    var payload: String = payload
        protected set

    override fun toEntity(): TestEntity =
        TestEntity(
            BinaryId.fromString(entityId),
            createdAt,
            updatedAt,
            payload,
        )

    override fun update(entity: TestEntity) {
        this.payload = entity.payload
        this.createdAt = entity.createdAt
        this.updatedAt = entity.updatedAt
    }

    companion object {
        fun of(entity: TestEntity): TestEntityDocumentModel =
            TestEntityDocumentModel(
                entity.id.toString(),
                entity.createdAt,
                entity.updatedAt,
                entity.payload,
            )
    }
}
