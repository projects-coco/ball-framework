package org.coco.example.infra.jpa.model.rental

import jakarta.persistence.*
import org.coco.core.type.BinaryId
import org.coco.core.utils.currentClock
import org.coco.example.domain.model.rental.Rental
import org.coco.infra.jpa.model.DataModel
import org.hibernate.envers.Audited
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "rentals")
@Audited
class RentalDataModel(
    id: BinaryId,
    item: ItemDataModel,
    beginAt: LocalDate = LocalDate.now(currentClock()),
    endAt: LocalDate = beginAt.plusDays(7),
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : DataModel<Rental>(id.value, createdAt, updatedAt) {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    var item: ItemDataModel = item
        protected set

    @Column(nullable = false)
    var beginAt: LocalDate = beginAt
        protected set

    @Column(nullable = false)
    var endAt: LocalDate = endAt
        protected set

    override fun toEntity(): Rental {
        TODO("Not yet implemented")
    }

    override fun update(entity: Rental) {
        TODO("Not yet implemented")
    }

    companion object {
        fun of(rental: Rental): RentalDataModel {
            return RentalDataModel(
                rental.id,
                ItemDataModel.of(rental.item),
                beginAt = rental.beginAt,
                endAt = rental.endAt,
                createdAt = rental.createdAt,
                updatedAt = rental.updatedAt,
            )
        }
    }
}
