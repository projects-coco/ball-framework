package org.coco.domain.service.auth

import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.domain.model.auth.RefreshToken
import org.coco.domain.model.auth.RefreshTokenRepository
import org.coco.domain.model.auth.Token
import org.coco.domain.model.auth.UserPrincipal

class RefreshTokenHandler(
    private val refreshTokenProvider: TokenProvider<UserPrincipal>,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun issue(userPrincipal: UserPrincipal): RefreshToken {
        val payload = refreshTokenProvider.generateToken(userPrincipal)
        val expiredAt = refreshTokenProvider.getExpiredAt(payload)
        val refreshToken =
            RefreshToken(
                userId = userPrincipal.id,
                payload = payload,
                used = false,
                expiredAt = expiredAt,
            )
        return refreshTokenRepository.save(refreshToken)
    }

    fun consume(payload: Token.Payload): UserPrincipal =
        refreshTokenProvider
            .verify(payload)
            .fold(
                { throw LogicError("유효한 인증 정보가 없습니다.", ErrorType.NOT_FOUND) },
                {
                    val refreshTokenId =
                        refreshTokenRepository
                            .findByPayload(payload)
                            .orElseThrow {
                                LogicError("유효한 인증 정보가 없습니다.", ErrorType.NOT_FOUND)
                            }.id
                    refreshTokenRepository.update(refreshTokenId) { refreshToken ->
                        refreshToken.consume()
                    }
                    it
                },
            )
}
