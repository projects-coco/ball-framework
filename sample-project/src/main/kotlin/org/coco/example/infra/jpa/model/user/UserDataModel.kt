package org.coco.example.infra.jpa.model.user

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.coco.core.type.BinaryId
import org.coco.domain.model.user.Agreement
import org.coco.domain.model.user.BasicUser.*
import org.coco.example.domain.model.user.User
import org.coco.infra.jpa.model.user.BasicUserDataModel
import org.hibernate.annotations.Type
import org.hibernate.envers.Audited
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@Audited
class UserDataModel(
    id: BinaryId,
    username: Username,
    roles: Set<User.Role>,
    name: Name,
    phoneNumber: PhoneNumber,
    passwordHash: PasswordHash,
    agreementOfTerms: Agreement,
    agreementOfPrivacy: Agreement,
    active: Boolean,
    lastLoginAt: LocalDateTime?,
    loginCount: Long,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : BasicUserDataModel<User>(
        id,
        username,
        roles,
        name,
        phoneNumber,
        passwordHash,
        agreementOfTerms,
        agreementOfPrivacy,
        active,
        lastLoginAt,
        loginCount,
        createdAt,
        updatedAt,
    ) {
    @Type(JsonType::class)
    @Column(columnDefinition = "json")
    override var roles: Set<String> = roles.map { it.name }.toSet()

    override fun update(entity: User) {
        this.username = entity.username.value
        this.roles = entity.roles.map { it.toString() }.toSet()
        this.name = entity.name.value
        this.phoneNumber = entity.phoneNumber.value
        this.passwordHash = entity.passwordHash.value
        this.agreementOfTerms = entity.agreementOfTerms
        this.agreementOfPrivacy = entity.agreementOfPrivacy
    }

    companion object {
        fun of(entity: User): UserDataModel {
            @Suppress("UNCHECKED_CAST")
            return UserDataModel(
                id = entity.id,
                username = entity.username,
                roles = entity.roles as Set<User.Role>,
                name = entity.name,
                phoneNumber = entity.phoneNumber,
                passwordHash = entity.passwordHash,
                agreementOfTerms = entity.agreementOfTerms,
                agreementOfPrivacy = entity.agreementOfPrivacy,
                active = entity.active,
                lastLoginAt = entity.lastLoginAt,
                loginCount = entity.loginCount,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}
