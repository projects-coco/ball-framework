package org.coco.domain.model.revision

import org.coco.core.type.BinaryId
import org.coco.domain.model.user.BasicUser
import java.time.Instant

interface BallRevisionMetadata {
    fun getRevisionNumber(): Long

    fun getRevisionInstant(): Instant

    fun getAuthorId(): BinaryId?

    fun getAuthor(): BasicUser.Username?
}
