package org.coco.example.domain.model.history

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.coco.example.domain.model.rental.Rental
import org.coco.example.domain.model.user.User
import org.hibernate.envers.Audited

@Entity
@Audited
class RentalHistory(
    id: BinaryId = BinaryId.new(),
    user: User,
    rental: Rental,
) : EntityBase(id) {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    var user: User = user
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    var rental: Rental = rental
        protected set
}
