package org.coco.example.infra.jpa

import org.coco.core.type.BinaryId
import org.coco.example.domain.model.rental.Item
import org.coco.example.domain.model.rental.ItemRepository
import org.coco.example.infra.jpa.model.rental.ItemDataModel
import org.coco.example.infra.jpa.model.rental.ItemJpaRepository
import org.coco.infra.jpa.helper.JpaRepositoryHelper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class ItemRepositoryImpl(
    private val jpaRepository: ItemJpaRepository,
) : JpaRepositoryHelper<Item, ItemDataModel>(jpaRepository, Item::class),
    ItemRepository {
    override fun ItemDataModel.toEntity(): Item =
        Item(
            id = BinaryId(id),
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    override fun save(entity: Item): Item {
        val dataModel = ItemDataModel.of(entity)
        return jpaRepository.save(dataModel).toEntity()
    }
}
