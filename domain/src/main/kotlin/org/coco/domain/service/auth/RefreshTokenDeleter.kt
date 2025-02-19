package org.coco.domain.service.auth

import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.domain.model.auth.RefreshTokenRepository
import org.coco.domain.model.auth.Token

class RefreshTokenDeleter(
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun execute(refreshTokenPayload: Token.Payload) {
        val refreshToken =
            refreshTokenRepository.findByPayload(refreshTokenPayload).orElseThrow {
                LogicError("로그아웃 할 수 없습니다", ErrorType.BAD_REQUEST)
            }
        refreshTokenRepository.delete(refreshToken.id)
    }
}
