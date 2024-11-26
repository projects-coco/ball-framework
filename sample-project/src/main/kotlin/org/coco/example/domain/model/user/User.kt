package org.coco.example.domain.model.user

import arrow.core.Option
import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.coco.domain.core.toOption
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

    fun update(
        username: Option<String> = null.toOption(),
    ) {
        username.onSome { this.username = it }
    }
}
