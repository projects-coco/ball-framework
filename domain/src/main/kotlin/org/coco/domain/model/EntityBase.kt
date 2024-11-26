package org.coco.domain.model

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.coco.domain.utils.currentClock
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class EntityBase(
    id: BinaryId,
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
    version: Long = 1,
) {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", unique = true, nullable = false, updatable = false)
    open var id: BinaryId = id
        protected set

    @CreatedDate
    open var createdAt: LocalDateTime? = createdAt
        protected set

    @LastModifiedDate
    open var updatedAt: LocalDateTime? = updatedAt
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
