package org.coco.infra.jpa.model.domain

import org.coco.core.type.BinaryId
import org.coco.domain.model.EntityBase
import java.time.LocalDateTime

class TestEntity(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    payload: String,
) : EntityBase(id, createdAt, updatedAt) {
    var payload: String = payload
}
