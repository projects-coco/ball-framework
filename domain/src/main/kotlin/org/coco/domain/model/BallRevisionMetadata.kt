package org.coco.domain.model

import org.coco.domain.model.user.BasicUser
import java.time.Instant

interface BallRevisionMetadata {
    fun getRevisionNumber(): Long

    fun getRevisionInstant(): Instant

    fun getAuthorId(): BinaryId?

    fun getAuthor(): BasicUser.Username?
}