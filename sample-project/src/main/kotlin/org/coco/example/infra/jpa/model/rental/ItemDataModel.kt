package org.coco.example.infra.jpa.model.rental

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.coco.core.type.BinaryId
import org.coco.example.domain.model.rental.Item
import org.coco.infra.jpa.model.DataModel
import org.hibernate.envers.Audited
import java.time.LocalDateTime

@Entity
@Table(name = "items")
@Audited
class ItemDataModel(
    id: BinaryId = BinaryId.new(),
    name: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : DataModel<Item>(id.value, createdAt, updatedAt) {
    @Column(nullable = false, columnDefinition = "varchar(255)", length = 255)
    var name: String = name
        protected set

    override fun update(entity: Item) {
        this.name = entity.name
    }

    companion object {
        fun of(item: Item): ItemDataModel =
            ItemDataModel(
                item.id,
                item.name,
                item.createdAt,
                item.updatedAt,
            )
    }
}
