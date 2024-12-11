package org.coco.example.infra.jpa.model.history

import org.coco.infra.jpa.helper.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface RentalHistoryJpaRepository : JpaRevisionRepository<RentalHistoryDataModel>
