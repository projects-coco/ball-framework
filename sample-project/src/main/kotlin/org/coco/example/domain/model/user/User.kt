package org.coco.example.domain.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.hibernate.envers.Audited

@Entity
@Audited
class User(
    id: BinaryId = BinaryId.new(),
    username: String,
) : EntityBase(id) {
    @Column(unique = true)
    var username: String = username
        protected set
}
