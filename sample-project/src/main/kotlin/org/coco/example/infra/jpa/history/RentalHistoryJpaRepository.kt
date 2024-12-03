package org.coco.example.infra.jpa.history

import org.coco.infra.jpa.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface RentalHistoryJpaRepository : JpaRevisionRepository<RentalHistoryDataModel>
