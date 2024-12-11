package org.coco.infra.jpa.model

import jakarta.persistence.*
import org.coco.infra.jpa.core.BallAuditRevisionListener
import org.hibernate.envers.RevisionEntity
import org.hibernate.envers.RevisionNumber
import org.hibernate.envers.RevisionTimestamp
import java.io.Serializable

@Entity
@Table(name = "_revision_info")
@RevisionEntity(BallAuditRevisionListener::class)
class BallRevisionEntity(
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

    var userId: ByteArray? = null
        internal set

    var username: String? = null
        internal set
}