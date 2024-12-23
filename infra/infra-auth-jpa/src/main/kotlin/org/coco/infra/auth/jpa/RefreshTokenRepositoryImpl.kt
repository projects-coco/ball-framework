package org.coco.infra.auth.jpa

import org.coco.core.type.BinaryId
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
) : JpaRepositoryHelper<RefreshToken, RefreshTokenDataModel>(jpaRepository, RefreshToken::class),
    RefreshTokenRepository {
    override fun RefreshTokenDataModel.toEntity(): RefreshToken =
        RefreshToken(
            id = BinaryId(id),
            userId = BinaryId(userId),
            payload = Token.Payload(payload),
            used = used,
            expiredAt = expiredAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    override fun RefreshToken.toModel(): RefreshTokenDataModel =
        RefreshTokenDataModel(
            id = id,
            userId = userId,
            payload = payload,
            used = used,
            expiredAt = expiredAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    override fun findByPayload(payload: Token.Payload): Optional<RefreshToken> =
        jpaRepository.findByPayload(payload.value).map { it.toEntity() }
}
