package org.coco.infra.auth.redis

import org.coco.core.type.BinaryId
import org.coco.domain.model.auth.RefreshToken
import org.coco.domain.model.auth.RefreshTokenRepository
import org.coco.domain.model.auth.Token
import org.coco.infra.auth.redis.model.RefreshTokenHashModel
import org.coco.infra.auth.redis.model.RefreshTokenRedisRepository
import org.coco.infra.redis.helper.RedisRepositoryHelper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RefreshTokenRepositoryImpl(
    private val redisRepository: RefreshTokenRedisRepository,
) : RedisRepositoryHelper<RefreshToken, RefreshTokenHashModel>(redisRepository, RefreshToken::class),
    RefreshTokenRepository {
    override fun RefreshTokenHashModel.toEntity(): RefreshToken =
        RefreshToken(
            id = BinaryId.fromString(id),
            userId = BinaryId.fromString(userId),
            payload = Token.Payload(payload),
            used = used,
            expiredAt = expiredAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    override fun RefreshToken.toModel(): RefreshTokenHashModel = RefreshTokenHashModel.of(this)

    override fun findByPayload(payload: Token.Payload): Optional<RefreshToken> =
        redisRepository.findByPayload(payload.value).map { it.toEntity() }
}
