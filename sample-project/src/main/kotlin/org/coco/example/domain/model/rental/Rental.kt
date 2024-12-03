package org.coco.example.domain.model.rental

import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.coco.domain.utils.currentClock
import java.time.LocalDate
import java.time.LocalDateTime

class Rental(
    id: BinaryId = BinaryId.new(),
    item: Item,
    beginAt: LocalDate = LocalDate.now(currentClock()),
    endAt: LocalDate = beginAt.plusDays(7),
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
): EntityBase(id, createdAt, updatedAt) {
    var item: Item = item
        private set

    var beginAt: LocalDate = beginAt
        private set

    var endAt: LocalDate = endAt
        private set
}
