package org.coco.example.domain.model.rental

import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.hibernate.envers.Audited

@Entity
@Audited
class Item(
    id: BinaryId = BinaryId.new(),
    name: String,
) : EntityBase(id) {
    @Column(nullable = false, columnDefinition = "varchar(255)", length = 255)
    var name: String = name
        protected set
}
