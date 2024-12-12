package org.coco.infra.auth.redis.model

import org.coco.core.type.BinaryId
import org.coco.domain.model.auth.RefreshToken
import org.coco.domain.model.auth.Token
import org.coco.infra.redis.model.HashModel
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime

@RedisHash(value = "refresh_tokens", timeToLive = 60 * 60 * 24 * 30)
class RefreshTokenHashModel(
    id: String,
    userId: String,
    payload: String,
    used: Boolean,
    expiredAt: LocalDateTime,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : HashModel<RefreshToken>(id, createdAt, updatedAt) {
    var userId: String = userId
        private set

    @Indexed
    var payload: String = payload
        private set

    var used: Boolean = used
        private set

    var expiredAt: LocalDateTime = expiredAt
        private set

    override fun toEntity(): RefreshToken =
        RefreshToken(
            id = BinaryId.fromString(id),
            userId = BinaryId.fromString(userId),
            payload = Token.Payload(payload),
            used = used,
            expiredAt = expiredAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    override fun update(entity: RefreshToken) {
        userId = entity.userId.toString()
        payload = entity.payload.value
        used = entity.used
        expiredAt = entity.expiredAt
    }

    companion object {
        fun of(entity: RefreshToken): RefreshTokenHashModel =
            RefreshTokenHashModel(
                id = entity.id.toString(),
                userId = entity.userId.toString(),
                payload = entity.payload.value,
                used = entity.used,
                expiredAt = entity.expiredAt,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
    }
}
