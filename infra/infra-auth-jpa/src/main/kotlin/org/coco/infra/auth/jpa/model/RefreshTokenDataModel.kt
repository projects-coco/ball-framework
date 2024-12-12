package org.coco.infra.auth.jpa.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.coco.core.type.BinaryId
import org.coco.domain.model.auth.RefreshToken
import org.coco.domain.model.auth.Token
import org.coco.infra.jpa.model.DataModel
import java.time.LocalDateTime

@Entity
@Table(name = "refresh_tokens")
class RefreshTokenDataModel(
    id: BinaryId,
    userId: BinaryId,
    payload: Token.Payload,
    used: Boolean,
    expiredAt: LocalDateTime,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : DataModel<RefreshToken>(id.value, createdAt, updatedAt) {
    @Column(columnDefinition = "binary(16)")
    var userId: ByteArray = userId.value
        protected set

    @Column(columnDefinition = "varchar(512)")
    var payload: String = payload.value
        protected set

    @Column(columnDefinition = "boolean default false")
    var used: Boolean = used
        protected set

    var expiredAt: LocalDateTime = expiredAt
        protected set

    override fun toEntity(): RefreshToken =
        RefreshToken(
            id = BinaryId(id),
            userId = BinaryId(userId),
            payload = Token.Payload(payload),
            used = used,
            expiredAt = expiredAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    override fun update(entity: RefreshToken) {
        userId = entity.userId.value
        payload = entity.payload.value
        used = entity.used
        expiredAt = entity.expiredAt
    }
}
