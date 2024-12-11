package org.coco.example.infra.jpa

import org.coco.example.domain.model.history.RentalHistory
import org.coco.example.domain.model.history.RentalHistoryRepository
import org.coco.example.infra.jpa.model.history.RentalHistoryDataModel
import org.coco.example.infra.jpa.model.history.RentalHistoryJpaRepository
import org.coco.infra.jpa.helper.JpaRepositoryHelper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class RentalHistoryRepositoryImpl(
    private val jpaRepository: RentalHistoryJpaRepository,
) : RentalHistoryRepository,
    JpaRepositoryHelper<RentalHistory, RentalHistoryDataModel>(jpaRepository, RentalHistory::class) {
    override fun save(entity: RentalHistory): RentalHistory {
        val dataModel = RentalHistoryDataModel.of(entity)
        return jpaRepository.save(dataModel).toEntity()
    }
}
