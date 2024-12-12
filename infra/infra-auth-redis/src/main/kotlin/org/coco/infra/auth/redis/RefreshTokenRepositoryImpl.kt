package org.coco.infra.auth.redis

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
    override fun save(entity: RefreshToken): RefreshToken {
        val hashModel = RefreshTokenHashModel.of(entity)
        return redisRepository.save(hashModel).toEntity()
    }

    override fun findByPayload(payload: Token.Payload): Optional<RefreshToken> =
        redisRepository.findByPayload(payload.value).map { it.toEntity() }
}
