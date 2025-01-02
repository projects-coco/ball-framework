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
        roles.map { it.toString() }.toSet(),
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
    enum class Role {
        ROLE_ADMIN,
        ROLE_USER,
    }

    @JvmInline
    value class Password(
        override val value: String,
    ) : IPassword {
        init {
            val passwordMinLength = 4
            require(value.length >= passwordMinLength) { "최소 4자 이상의 패스워드를 입력해주세요." }
        }
    }

    fun updateName(name: Name) {
        this.name = name
    }
}
