package org.coco.domain.model

import org.coco.core.type.BinaryId
import java.time.LocalDateTime

abstract class EntityBase(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    open var id: BinaryId = id
        protected set

    open var createdAt: LocalDateTime = createdAt
        protected set

    open var updatedAt: LocalDateTime = updatedAt
        protected set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this.javaClass != other?.javaClass) return false
        other as EntityBase
        return id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
