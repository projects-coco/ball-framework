package org.coco.example.infra.jpa.rental

import org.coco.infra.jpa.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface RentalJpaRepository : JpaRevisionRepository<RentalDataModel>
