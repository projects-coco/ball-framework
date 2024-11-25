package org.coco.example.infra.jpa

import org.coco.example.domain.model.history.RentalHistory
import org.coco.example.domain.model.history.RentalHistoryRepository
import org.coco.infra.jpa.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface RentalHistoryJpaRepository :
    RentalHistoryRepository,
    JpaRevisionRepository<RentalHistory>
