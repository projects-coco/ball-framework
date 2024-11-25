package org.coco.example.domain.model.rental

import jakarta.persistence.*
import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.coco.domain.utils.currentClock
import org.hibernate.envers.Audited
import java.time.LocalDate

@Entity
@Audited
class Rental(
    id: BinaryId = BinaryId.new(),
    item: Item,
    beginAt: LocalDate = LocalDate.now(currentClock()),
    endAt: LocalDate = beginAt.plusDays(7),
): EntityBase(id) {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    var item: Item = item
        protected set

    @Column(nullable = false)
    var beginAt: LocalDate = beginAt
        protected set

    @Column(nullable = false)
    var endAt: LocalDate = endAt
        protected set
}
