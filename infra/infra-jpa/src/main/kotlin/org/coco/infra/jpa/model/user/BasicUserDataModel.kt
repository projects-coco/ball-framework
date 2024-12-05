package org.coco.infra.jpa.model.user

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.MappedSuperclass
import org.coco.domain.model.BinaryId
import org.coco.domain.model.user.BasicUser
import org.coco.domain.model.user.BasicUser.*
import org.coco.infra.jpa.model.DataModel
import java.time.LocalDateTime

@MappedSuperclass
abstract class BasicUserDataModel<T : BasicUser>(
    id: BinaryId,
    username: Username,
    roles: Set<IRole>,
    name: Name,
    phoneNumber: PhoneNumber,
    passwordHash: PasswordHash,
    agreementOfTerms: Agreement,
    agreementOfPrivacy: Agreement,
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
    var agreementOfTerms: Agreement = agreementOfTerms
        protected set

    @Embedded
    var agreementOfPrivacy: Agreement = agreementOfPrivacy
        protected set
}