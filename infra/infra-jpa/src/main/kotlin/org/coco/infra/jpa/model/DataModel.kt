package org.coco.infra.jpa.model

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.coco.domain.utils.currentClock
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class DataModel<T : EntityBase>(
    id: BinaryId,
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
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

    abstract fun toEntity(): T

    abstract fun update(entity: T)
}