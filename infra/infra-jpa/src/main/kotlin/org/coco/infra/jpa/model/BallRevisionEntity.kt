package org.coco.infra.jpa.model

import jakarta.persistence.*
import org.coco.domain.model.BallRevision
import org.coco.domain.model.BinaryId
import org.coco.domain.model.user.BasicUser
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
) : Serializable, BallRevision {
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

    override fun getAuthorId(): BinaryId? {
        return authorId?.let { BinaryId(it) }
    }

    override fun getAuthor(): BasicUser.Username? {
        return author?.let { BasicUser.Username(it) }
    }
}