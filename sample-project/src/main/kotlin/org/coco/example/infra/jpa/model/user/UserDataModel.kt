package org.coco.example.infra.jpa.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.coco.domain.model.BinaryId
import org.coco.example.domain.model.user.User
import org.coco.infra.jpa.model.DataModel
import org.hibernate.envers.Audited

@Entity
@Table(name = "users")
@Audited
class UserDataModel(
    id: BinaryId,
    username: String,
) : DataModel<User>(id.value) {
    @Column(unique = true)
    var username: String = username
        protected set

    override fun toEntity(): User = User(BinaryId(id), username, createdAt, updatedAt)

    override fun update(entity: User) {
        if (entity.username != username) {
            username = entity.username
        }
    }

    companion object {
        fun of(user: User): UserDataModel {
            return UserDataModel(
                id = user.id,
                username = user.username
            )
        }
    }
}
