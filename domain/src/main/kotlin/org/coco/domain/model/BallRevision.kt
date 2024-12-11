package org.coco.domain.model

import org.coco.domain.model.user.BasicUser

interface BallRevision {
    fun getAuthorId(): BinaryId?

    fun getAuthor(): BasicUser.Username?
}