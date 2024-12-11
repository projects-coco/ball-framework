package org.coco.domain.model.auth

import org.coco.core.type.BinaryId
import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.core.utils.currentClock
import org.coco.domain.model.EntityBase
import java.time.LocalDateTime

class RefreshToken(
    id: BinaryId = BinaryId.new(),
    userId: BinaryId,
    payload: Token.Payload,
    used: Boolean,
    expiredAt: LocalDateTime,
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
) : EntityBase(id, createdAt, updatedAt) {
    var userId: BinaryId = userId
        private set

    var payload: Token.Payload = payload
        private set

    var used: Boolean = used
        private set

    var expiredAt: LocalDateTime = expiredAt
        private set

    fun consume() {
        if (used || expiredAt.isAfter(LocalDateTime.now(currentClock()))) {
            throw LogicError("이미 만료된 인증정보입니다.", ErrorType.BAD_REQUEST)
        }
        used = true
    }
}
