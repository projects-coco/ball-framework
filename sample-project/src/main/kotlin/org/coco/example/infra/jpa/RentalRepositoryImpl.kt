package org.coco.example.infra.jpa

import org.coco.example.domain.model.rental.Rental
import org.coco.example.domain.model.rental.RentalRepository
import org.coco.example.infra.jpa.model.rental.RentalDataModel
import org.coco.example.infra.jpa.model.rental.RentalJpaRepository
import org.coco.infra.jpa.helper.JpaRepositoryHelper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class RentalRepositoryImpl(
    private val jpaRepository: RentalJpaRepository,
) : RentalRepository, JpaRepositoryHelper<Rental, RentalDataModel>(jpaRepository, Rental::class) {
    override fun save(entity: Rental): Rental {
        val dataModel = RentalDataModel.of(rental = entity)
        return jpaRepository.save(dataModel).toEntity()
    }
}