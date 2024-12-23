package org.coco.example.infra.jpa

import org.coco.core.type.BinaryId
import org.coco.example.domain.model.history.RentalHistory
import org.coco.example.domain.model.history.RentalHistoryRepository
import org.coco.example.domain.model.rental.RentalRepository
import org.coco.example.domain.model.user.UserRepository
import org.coco.example.infra.jpa.model.history.RentalHistoryDataModel
import org.coco.example.infra.jpa.model.history.RentalHistoryJpaRepository
import org.coco.infra.jpa.helper.JpaRepositoryHelper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class RentalHistoryRepositoryImpl(
    private val jpaRepository: RentalHistoryJpaRepository,
    private val userRepository: UserRepository,
    private val rentalRepository: RentalRepository,
) : JpaRepositoryHelper<RentalHistory, RentalHistoryDataModel>(jpaRepository, RentalHistory::class),
    RentalHistoryRepository {
    override fun RentalHistoryDataModel.toEntity(): RentalHistory {
        val user = userRepository.findById(BinaryId(this.user.id)).orElseThrow()
        val rental = rentalRepository.findById(BinaryId(this.rental.id)).orElseThrow()
        return RentalHistory(
            id = BinaryId(id),
            user = user,
            rental = rental,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    override fun save(entity: RentalHistory): RentalHistory {
        val dataModel = RentalHistoryDataModel.of(entity)
        return jpaRepository.save(dataModel).toEntity()
    }
}
