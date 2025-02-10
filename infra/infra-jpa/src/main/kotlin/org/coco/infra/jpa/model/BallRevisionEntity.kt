package org.coco.infra.jpa.model

import jakarta.persistence.*
import org.coco.core.type.BinaryId
import org.coco.domain.model.revision.BallRevisionMetadata
import org.coco.domain.model.user.vo.Username
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
    timestamp: Long,
) : Serializable,
    BallRevisionMetadata {
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

    override fun getRevisionNumber(): Long = rev

    override fun getRevisionInstant(): Instant = Instant.ofEpochMilli(timestamp)

    override fun getAuthorId(): BinaryId? = authorId?.let { BinaryId(it) }

    override fun getAuthor(): Username? = author?.let { Username(it) }
}
