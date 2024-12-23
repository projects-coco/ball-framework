package org.coco.infra.jpa.model.user

import jakarta.persistence.*
import org.coco.core.type.BinaryId
import org.coco.domain.model.user.Agreement
import org.coco.domain.model.user.BasicUser
import org.coco.domain.model.user.BasicUser.*
import org.coco.infra.jpa.model.DataModel
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import java.time.LocalDateTime

@MappedSuperclass
@Audited
abstract class BasicUserDataModel<T : BasicUser>(
    id: BinaryId,
    username: Username,
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
) : DataModel<T>(id.value, createdAt, updatedAt) {
    @Column(columnDefinition = "varchar(32)", unique = true)
    var username: String = username.value
        protected set

    abstract var roles: Set<String>
        protected set

    @Column(columnDefinition = "varchar(64)")
    var name: String = name.value

    @Column(columnDefinition = "varchar(16)")
    var phoneNumber: String = phoneNumber.value

    @Column(columnDefinition = "varchar(128)")
    var passwordHash: String = passwordHash.value

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "status", column = Column(name = "agreement_of_terms_status")),
        AttributeOverride(name = "agreeAt", column = Column(name = "agreement_of_terms_agree_at")),
    )
    var agreementOfTerms: Agreement = agreementOfTerms
        protected set

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "status", column = Column(name = "agreement_of_privacy_status")),
        AttributeOverride(name = "agreeAt", column = Column(name = "agreement_of_privacy_agree_at")),
    )
    var agreementOfPrivacy: Agreement = agreementOfPrivacy
        protected set

    @Column(columnDefinition = "boolean default true")
    var active: Boolean = active
        protected set

    @NotAudited
    var lastLoginAt: LocalDateTime? = lastLoginAt
        protected set

    @NotAudited
    var loginCount: Long = loginCount
        protected set
}
