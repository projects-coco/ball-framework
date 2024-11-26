package org.coco.example.infra.jpa

import org.coco.example.domain.model.rental.Rental
import org.coco.example.domain.model.rental.RentalRepository
import org.coco.infra.jpa.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface RentalJpaRepository :
    RentalRepository,
    JpaRevisionRepository<Rental>
