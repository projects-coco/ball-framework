package org.coco.example.domain.model.user

import org.coco.core.type.BinaryId
import org.coco.core.utils.currentClock
import org.coco.domain.model.user.Agreement
import org.coco.domain.model.user.BasicUser
import java.time.LocalDateTime

class User(
    id: BinaryId = BinaryId.new(),
    username: Username,
    roles: Set<Role>,
    name: Name,
    phoneNumber: PhoneNumber,
    passwordHash: PasswordHash,
    agreementOfTerms: Agreement = Agreement.agree(),
    agreementOfPrivacy: Agreement = Agreement.agree(),
    active: Boolean = true,
    lastLoginAt: LocalDateTime? = null,
    loginCount: Long = 0,
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
) : BasicUser(
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
    updatedAt
) {
    enum class Role : IRole {
        ROLE_ADMIN,
        ROLE_USER
    }

    fun updateName(name: Name) {
        this.name = name
    }
}
