package org.coco.infra.auth.jpa

import org.coco.domain.model.auth.RefreshToken
import org.coco.domain.model.auth.RefreshTokenRepository
import org.coco.domain.model.auth.Token
import org.coco.infra.auth.jpa.model.RefreshTokenDataModel
import org.coco.infra.auth.jpa.model.RefreshTokenJpaRepository
import org.coco.infra.jpa.helper.JpaRepositoryHelper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RefreshTokenRepositoryImpl(
    private val jpaRepository: RefreshTokenJpaRepository,
) : RefreshTokenRepository,
    JpaRepositoryHelper<RefreshToken, RefreshTokenDataModel>(jpaRepository, RefreshToken::class) {
    override fun findByPayload(payload: Token.Payload): Optional<RefreshToken> =
        jpaRepository.findByPayload(payload.value).map { it.toEntity() }

    override fun save(entity: RefreshToken): RefreshToken {
        val dataModel =
            RefreshTokenDataModel(
                id = entity.id,
                userId = entity.userId,
                payload = entity.payload,
                used = entity.used,
                expiredAt = entity.expiredAt,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        return jpaRepository.save(dataModel).toEntity()
    }
}
