package org.coco.example.domain.model.history

import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.coco.core.utils.currentClock
import org.coco.example.domain.model.rental.Rental
import org.coco.example.domain.model.user.User
import java.time.LocalDateTime

class RentalHistory(
    id: BinaryId = BinaryId.new(),
    user: User,
    rental: Rental,
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
) : EntityBase(id, createdAt, updatedAt) {
    var user: User = user
        private set

    var rental: Rental = rental
        private set
}
