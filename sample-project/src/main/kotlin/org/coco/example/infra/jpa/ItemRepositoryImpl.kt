package org.coco.example.infra.jpa

import org.coco.example.domain.model.rental.Item
import org.coco.example.domain.model.rental.ItemRepository
import org.coco.example.infra.jpa.rental.ItemDataModel
import org.coco.example.infra.jpa.rental.ItemJpaRepository
import org.coco.infra.jpa.JpaRepositoryHelper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class ItemRepositoryImpl(
    private val jpaRepository: ItemJpaRepository
): ItemRepository, JpaRepositoryHelper<Item, ItemDataModel>(jpaRepository, Item::class) {
    override fun save(entity: Item): Item {
        val dataModel = ItemDataModel(id = entity.id, name = entity.name)
        return jpaRepository.save(dataModel).toEntity()
    }
}