package org.coco.domain.model.auth

import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.coco.domain.model.user.BasicUser
import org.coco.domain.utils.currentClock
import java.time.LocalDateTime

class RefreshToken(
    id: BinaryId = BinaryId.new(),
    user: BasicUser,
    payload: Token.Payload,
    used: Boolean,
    expiredAt: LocalDateTime,
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
) : EntityBase(id, createdAt, updatedAt) {
    var user: BasicUser = user
        private set

    var payload: Token.Payload = payload
        private set

    var used: Boolean = used
        private set

    var expiredAt: LocalDateTime = expiredAt
        private set
}