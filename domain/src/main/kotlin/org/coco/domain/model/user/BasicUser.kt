package org.coco.domain.model.user

import org.coco.core.utils.currentClock
import org.coco.domain.model.BinaryId
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

    @JvmInline
    value class Username(val value: String) {
        init {
            require(value.isNotBlank()) { "Username cannot be blank" }
        }
    }

    interface IRole

    enum class Role : IRole {
        USER, ADMIN
    }

    @JvmInline
    value class Name(val value: String) {
        init {
            require(value.isNotBlank()) { "Name cannot be blank" }
        }
    }

    @JvmInline
    value class Password(
        val value: String,
    ) {
        init {
            val minLength = 2
            // val passwordComplexityRegex = Regex("^(?=.*?[a-zA-Z])(?=.*?[0-9])(?=.*?[#?!@%^&*-]).{8,15}\$")
            require(value.length >= minLength) { "최소 ${minLength}자 이상의 패스워드를 입력해주세요." }
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
            require(phoneNumberRegex.matches(value)) { "전화번호 양식을 확인해주세요." }
        }
    }
}