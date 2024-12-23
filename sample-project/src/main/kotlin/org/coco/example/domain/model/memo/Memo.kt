package org.coco.example.domain.model.memo

import org.coco.core.type.BinaryId
import org.coco.core.utils.ToStringBuilder
import org.coco.core.utils.currentClock
import org.coco.domain.model.EntityBase
import org.coco.example.domain.model.user.User
import java.time.LocalDateTime

class Memo(
    id: BinaryId = BinaryId.new(),
    targetId: BinaryId,
    writer: User,
    content: String,
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
) : EntityBase(id, createdAt, updatedAt) {
    var targetId: BinaryId = targetId
        private set

    var writer: User = writer
        private set

    var content: String = content
        private set

    override fun toString(): String =
        ToStringBuilder(this)
            .append("id", id)
            .append("targetId", targetId)
            .append("writer", writer)
            .append("content", content)
            .append("createdAt", createdAt)
            .append("updatedAt", updatedAt)
            .toString()
}
