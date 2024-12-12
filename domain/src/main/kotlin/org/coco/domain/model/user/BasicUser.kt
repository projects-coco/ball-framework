package org.coco.domain.model.user

import org.coco.core.type.BinaryId
import org.coco.core.utils.ToStringBuilder
import org.coco.core.utils.currentClock
import org.coco.domain.model.EntityBase
import java.time.LocalDateTime

open class BasicUser(
    id: BinaryId = BinaryId.new(),
    username: Username,
    roles: Set<IRole> = setOf(Role.USER),
    name: Name,
    phoneNumber: PhoneNumber,
    passwordHash: PasswordHash,
    agreementOfTerms: Agreement = Agreement.disagree(),
    agreementOfPrivacy: Agreement = Agreement.disagree(),
    active: Boolean = true,
    lastLoginAt: LocalDateTime? = null,
    loginCount: Long = 0,
    createdAt: LocalDateTime = LocalDateTime.now(currentClock()),
    updatedAt: LocalDateTime = LocalDateTime.now(currentClock()),
) : EntityBase(id, createdAt, updatedAt) {
    var username: Username = username
        protected set

    var roles: Set<IRole> = roles
        protected set

    var name: Name = name
        protected set

    var phoneNumber: PhoneNumber = phoneNumber
        protected set

    var passwordHash: PasswordHash = passwordHash
        protected set

    var agreementOfTerms: Agreement = agreementOfTerms
        protected set

    var agreementOfPrivacy: Agreement = agreementOfPrivacy
        protected set

    var active: Boolean = active
        protected set

    var lastLoginAt: LocalDateTime? = lastLoginAt
        protected set

    var loginCount: Long = loginCount
        protected set

    fun loggingLogin() {
        this.lastLoginAt = LocalDateTime.now(currentClock())
        this.loginCount += 1
    }

    @JvmInline
    value class Username(
        val value: String,
    ) {
        init {
            val usernameMinLength = 5
            require(value.length >= usernameMinLength) { "아이디는 최소 ${usernameMinLength}자 이상의 값이 필요합니다." }
        }
    }

    interface IRole

    enum class Role : IRole {
        USER,
        ADMIN,
    }

    @JvmInline
    value class Name(
        val value: String,
    ) {
        init {
            val nameMinLength = 2
            require(value.length >= nameMinLength) { "이름은 최소 ${nameMinLength}자 이상의 값이 필요합니다." }
        }
    }

    interface IPassword {
        val value: String
    }

    @JvmInline
    value class Password(
        override val value: String,
    ) : IPassword {
        init {
            val passwordComplexityRegex = Regex("^(?=.*?[a-zA-Z])(?=.*?[#?!@%^&*-]).{6,24}\$")
            require(passwordComplexityRegex.matches(value)) { "최소 하나 이상의 영문자, 특수문자(#?!@%^&*-)를 포함해 6자 이상 24자 이하의 패스워드를 입력해주세요." }
        }
    }

    @JvmInline
    value class PasswordHash(
        val value: String,
    )

    @JvmInline
    value class PhoneNumber(
        val value: String,
    ) {
        init {
            val phoneNumberRegex = Regex("^[0-9]{3}-+[0-9]{3,4}-+[0-9]{4}\$")
            require(phoneNumberRegex.matches(value)) { "휴대폰 번호 양식을 확인해주세요." }
        }
    }

    override fun toString(): String =
        ToStringBuilder(this)
            .append("id", id.toString())
            .append("username", username.value)
            .append("roles", roles)
            .toString()
}
