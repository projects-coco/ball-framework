package org.coco.example.infra.jpa.rental

import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.coco.domain.model.BinaryId
import org.coco.example.domain.model.rental.Item
import org.coco.infra.jpa.model.DataModel
import org.hibernate.envers.Audited

@Entity
@Audited
class ItemDataModel(
    id: BinaryId = BinaryId.new(),
    name: String,
) : DataModel<Item>(id.value) {
    @Column(nullable = false, columnDefinition = "varchar(255)", length = 255)
    var name: String = name
        protected set

    override fun toEntity(): Item {
        TODO("Not yet implemented")
    }

    override fun update(entity: Item) {
        TODO("Not yet implemented")
    }

    companion object {
        fun of(item: Item): ItemDataModel {
            return ItemDataModel(item.id, item.name)
        }
    }
}