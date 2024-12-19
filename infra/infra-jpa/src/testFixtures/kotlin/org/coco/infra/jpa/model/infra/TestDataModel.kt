package org.coco.infra.jpa.model.infra

import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.coco.core.type.BinaryId
import org.coco.infra.jpa.model.DataModel
import org.coco.infra.jpa.model.domain.TestEntity
import org.hibernate.envers.Audited
import java.time.LocalDateTime

@Entity
@Audited
class TestDataModel(
    id: ByteArray,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    payload: String,
) : DataModel<TestEntity>(id, createdAt, updatedAt) {
    @Column(name = "payload", columnDefinition = "varchar(255)")
    var payload: String = payload
        protected set

    override fun toEntity(): TestEntity = TestEntity(BinaryId(id), createdAt, updatedAt, payload)

    override fun update(entity: TestEntity) {
        this.payload = entity.payload
        this.createdAt = entity.createdAt
        this.updatedAt = entity.updatedAt
    }

    companion object {
        fun of(entity: TestEntity): TestDataModel =
            TestDataModel(
                entity.id.value,
                entity.createdAt,
                entity.updatedAt,
                entity.payload,
            )
    }
}
