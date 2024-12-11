package org.coco.infra.jpa.model

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.coco.domain.model.EntityBase
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@Audited
abstract class DataModel<T : EntityBase>(
    id: ByteArray,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", unique = true, nullable = false, updatable = false)
    var id: ByteArray = id
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