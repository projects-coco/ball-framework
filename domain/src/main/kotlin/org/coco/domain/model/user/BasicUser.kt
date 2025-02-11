package org.coco.domain.model.user

import org.coco.core.type.BinaryId
import org.coco.core.utils.HidingToResponse
import org.coco.core.utils.ToStringBuilder
import org.coco.core.utils.currentClock
import org.coco.domain.model.EntityBase
import org.coco.domain.model.user.vo.*
import java.time.LocalDateTime

open class BasicUser(
    id: BinaryId = BinaryId.new(),
    username: Username,
    email: Email? = null,
    roles: Set<String>,
    legalName: LegalName,
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

    var email: Email? = email
        protected set

    @HidingToResponse
    var rolesAsString: Set<String> = roles
        protected set

    var legalName: LegalName = legalName
        protected set

    var phoneNumber: PhoneNumber = phoneNumber
        protected set

    @HidingToResponse
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

    override fun toString(): String =
        ToStringBuilder(this)
            .append("id", id.toString())
            .append("username", username.value)
            .append("roles", rolesAsString)
            .toString()
}
