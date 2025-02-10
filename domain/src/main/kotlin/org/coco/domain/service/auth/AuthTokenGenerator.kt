package org.coco.domain.service.auth

import org.coco.domain.model.auth.Token
import org.coco.domain.model.auth.UserPrincipal

class AuthTokenGenerator(
    private val accessTokenProvider: TokenProvider<UserPrincipal>,
    private val refreshTokenHandler: RefreshTokenHandler,
) {
    fun generate(userPrincipal: UserPrincipal): Pair<Token.Payload, Token.Payload> {
        val accessToken = accessTokenProvider.generateToken(userPrincipal)
        val refreshToken = refreshTokenHandler.issue(userPrincipal)

        return Pair(accessToken, refreshToken.payload)
    }
}
