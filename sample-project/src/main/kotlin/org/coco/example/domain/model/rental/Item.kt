package org.coco.example.domain.model.rental

import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.coco.domain.utils.currentClock
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
