package org.coco.example.domain.model.user

import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.coco.core.utils.currentClock
import java.time.LocalDateTime

class User(
    id: BinaryId = BinaryId.new(),
    username: String,
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
) : EntityBase(id, createdAt, updatedAt) {
    var username: String = username
}
