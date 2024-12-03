package org.coco.example.infra.jpa.model.rental

import org.coco.infra.jpa.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemJpaRepository : JpaRevisionRepository<ItemDataModel>
