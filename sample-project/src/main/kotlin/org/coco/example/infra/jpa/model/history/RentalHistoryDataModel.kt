package org.coco.example.infra.jpa.model.history

import jakarta.persistence.*
import org.coco.core.type.BinaryId
import org.coco.example.domain.model.history.RentalHistory
import org.coco.example.infra.jpa.model.rental.RentalDataModel
import org.coco.example.infra.jpa.model.user.UserDataModel
import org.coco.infra.jpa.model.DataModel
import org.hibernate.envers.Audited
import java.time.LocalDateTime

@Entity
@Table(name = "rental_histories")
@Audited
class RentalHistoryDataModel(
    id: BinaryId = BinaryId.new(),
    user: UserDataModel,
    rental: RentalDataModel,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : DataModel<RentalHistory>(id.value, createdAt, updatedAt) {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    var user: UserDataModel = user
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    var rental: RentalDataModel = rental
        protected set

    override fun toEntity(): RentalHistory {
        TODO("Not yet implemented")
    }

    override fun update(entity: RentalHistory) {
        TODO("Not yet implemented")
    }

    companion object {
        fun of(history: RentalHistory): RentalHistoryDataModel {
            return RentalHistoryDataModel(
                id = history.id,
                user = UserDataModel.of(history.user),
                rental = RentalDataModel.of(history.rental),
                createdAt = history.createdAt,
                updatedAt = history.updatedAt,
            )
        }
    }
}
