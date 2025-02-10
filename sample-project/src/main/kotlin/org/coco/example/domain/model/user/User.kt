package org.coco.example.domain.model.user

import org.coco.core.type.BinaryId
import org.coco.core.utils.currentClock
import org.coco.domain.model.user.BasicUser
import org.coco.domain.model.user.vo.*
import java.time.LocalDateTime

class User(
    id: BinaryId = BinaryId.new(),
    username: Username,
    roles: Set<Role>,
    legalName: LegalName,
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
        roles.map { it.name }.toSet(),
        legalName,
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
    var roles: Set<Role> = roles
        private set

    enum class Role {
        ROLE_ADMIN,
        ROLE_USER,
        ;

        companion object {
            fun from(roles: Set<String>): Set<Role> = roles.map { valueOf(it) }.toSet()
        }
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

    fun updateName(legalName: LegalName) {
        this.legalName = legalName
    }
}
