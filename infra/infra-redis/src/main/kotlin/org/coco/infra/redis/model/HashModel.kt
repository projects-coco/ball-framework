package org.coco.infra.redis.model

import org.coco.domain.model.EntityBase
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

abstract class HashModel<T : EntityBase>(
    id: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    @Id
    var id: String = id
        protected set

    @CreatedDate
    var createdAt: LocalDateTime = createdAt
        protected set

    @LastModifiedDate
    var updatedAt: LocalDateTime = updatedAt
        protected set

    abstract fun toEntity(): T

    abstract fun update(entity: T)
}
