package org.coco.infra.mongodb.model

import org.coco.core.type.BinaryId
import org.coco.domain.TestEntity
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "test_entity")
class TestEntityDocumentModel(
    id: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    payload: String,
) : DocumentModel<TestEntity>(id, createdAt, updatedAt) {
    var payload: String = payload
        protected set

    override fun toEntity(): TestEntity =
        TestEntity(
            BinaryId.fromString(id),
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
