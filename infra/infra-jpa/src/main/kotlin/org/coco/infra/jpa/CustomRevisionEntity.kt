package org.coco.infra.jpa

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.envers.RevisionEntity
import org.hibernate.envers.RevisionNumber
import org.hibernate.envers.RevisionTimestamp
import java.io.Serializable

@Entity
@RevisionEntity
class CustomRevisionEntity(
    rev: Long,
    timestamp: Long
) : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    var rev: Long = rev
        protected set

    @RevisionTimestamp
    var timestamp: Long = timestamp
        protected set
}