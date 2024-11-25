package org.coco.example.infra.jpa

import org.coco.domain.model.BinaryId
import org.coco.example.domain.model.rental.Rental
import org.coco.example.domain.model.rental.RentalRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RentalJpaRepository :
    RentalRepository,
    JpaRepository<Rental, BinaryId>
