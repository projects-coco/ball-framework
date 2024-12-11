package org.coco.example.infra.jpa.model.rental

import org.coco.infra.jpa.helper.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface RentalJpaRepository : JpaRevisionRepository<RentalDataModel>
