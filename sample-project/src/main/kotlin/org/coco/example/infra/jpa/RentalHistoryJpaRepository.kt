package org.coco.example.infra.jpa

import org.coco.domain.model.BinaryId
import org.coco.example.domain.model.history.RentalHistory
import org.coco.example.domain.model.history.RentalHistoryRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RentalHistoryJpaRepository :
    RentalHistoryRepository,
    JpaRepository<RentalHistory, BinaryId>
