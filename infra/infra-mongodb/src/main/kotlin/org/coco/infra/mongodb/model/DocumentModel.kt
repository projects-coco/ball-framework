package org.coco.infra.mongodb.model

import org.bson.types.ObjectId
import org.coco.domain.model.EntityBase
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import java.time.LocalDateTime

abstract class DocumentModel<T : EntityBase>(
    entityId: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    @Id
    var id: ObjectId = ObjectId()
        protected set

    @Indexed(unique = true)
    var entityId: String = entityId
        protected set

    @CreatedDate
    var createdAt: LocalDateTime = createdAt
        protected set

    @LastModifiedDate
    var updatedAt: LocalDateTime = updatedAt
        protected set

    abstract fun update(entity: T)
}
