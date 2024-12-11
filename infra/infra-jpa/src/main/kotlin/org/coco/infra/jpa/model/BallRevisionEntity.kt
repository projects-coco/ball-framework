package org.coco.infra.jpa.model

import jakarta.persistence.*
import org.coco.core.type.BinaryId
import org.coco.domain.model.revision.BallRevisionMetadata
import org.coco.domain.model.user.BasicUser
import org.coco.infra.jpa.core.BallAuditRevisionListener
import org.hibernate.envers.RevisionEntity
import org.hibernate.envers.RevisionNumber
import org.hibernate.envers.RevisionTimestamp
import java.io.Serializable
import java.time.Instant

@Entity
@Table(name = "_revision_info")
@RevisionEntity(BallAuditRevisionListener::class)
class BallRevisionEntity(
    rev: Long,
    timestamp: Long
) : Serializable, BallRevisionMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    var rev: Long = rev
        protected set

    @RevisionTimestamp
    var timestamp: Long = timestamp
        protected set

    var authorId: ByteArray? = null
        internal set

    var author: String? = null
        internal set

    override fun getRevisionNumber(): Long {
        return rev
    }

    override fun getRevisionInstant(): Instant {
        return Instant.ofEpochMilli(timestamp)
    }

    override fun getAuthorId(): BinaryId? {
        return authorId?.let { BinaryId(it) }
    }

    override fun getAuthor(): BasicUser.Username? {
        return author?.let { BasicUser.Username(it) }
    }
}