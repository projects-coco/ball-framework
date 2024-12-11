package org.coco.example.domain.model.rental

import org.coco.core.type.BinaryId
import org.coco.core.utils.currentClock
import org.coco.domain.model.EntityBase
import java.time.LocalDateTime

class Item(
    id: BinaryId = BinaryId.new(),
    name: String,
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
) : EntityBase(id, createdAt, updatedAt) {
    var name: String = name
        private set
}
