package org.coco.example.infra.jpa

import org.coco.example.domain.model.rental.Item
import org.coco.example.domain.model.rental.ItemRepository
import org.coco.infra.jpa.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemJpaRepository :
    ItemRepository,
    JpaRevisionRepository<Item>
