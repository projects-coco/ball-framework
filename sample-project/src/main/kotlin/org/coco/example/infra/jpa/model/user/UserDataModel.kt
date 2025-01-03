package org.coco.example.infra.jpa.model.user

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import org.coco.core.type.BinaryId
import org.coco.domain.model.user.Agreement
import org.coco.domain.model.user.BasicUser.*
import org.coco.example.domain.model.user.User
import org.coco.infra.jpa.model.user.BasicUserDataModel
import org.hibernate.envers.Audited
import java.time.LocalDateTime

@Entity
@DiscriminatorValue("user")
@Audited
class UserDataModel(
    id: BinaryId,
    username: Username,
    roles: Set<String>,
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
    override fun update(entity: User) {
        this.username = entity.username.value
        this.roles = entity.rolesAsString.toSet()
        this.name = entity.name.value
        this.phoneNumber = entity.phoneNumber.value
        this.passwordHash = entity.passwordHash.value
        this.agreementOfTerms = entity.agreementOfTerms
        this.agreementOfPrivacy = entity.agreementOfPrivacy
    }

    companion object {
        fun of(entity: User): UserDataModel =
            UserDataModel(
                id = entity.id,
                username = entity.username,
                roles = entity.roles.map { it.name }.toSet(),
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
