package org.coco.domain.service.auth

import org.coco.core.utils.logger
import org.coco.domain.core.ErrorType
import org.coco.domain.core.LogicError
import org.coco.domain.model.auth.RefreshTokenRepository
import org.coco.domain.model.auth.Token
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.BasicUser
import org.coco.domain.model.user.BasicUserRepository

open class AuthService(
    private val accessTokenProvider: TokenProvider,
    private val refreshTokenHandler: RefreshTokenHandler,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: BasicUserRepository<*, *>,
    private val passwordHashProvider: PasswordHashProvider,
) {
    companion object {
        val LoginFailError = LogicError("로그인에 실패했습니다. 아이디/비밀번호를 다시 확인해주세요.", ErrorType.UNAUTHORIZED)
        val UserDisabledError = LogicError("사용이 중지된 계정입니다. 관리자에게 문의해주세요.", ErrorType.UNAUTHORIZED)
        val LogoutFailError = LogicError("로그아웃 할 수 없습니다", ErrorType.BAD_REQUEST)
    }

    protected val logger = logger()

    data class LoginCommand(
        val username: BasicUser.Username,
        val password: BasicUser.Password,
        val remoteIp: String,
    )

    fun login(command: LoginCommand): Pair<Token.Payload, Token.Payload> {
        val user = authenticate(command)
        userRepository.update(user.id) {
            it.loggingLogin()
        }

        val userPrincipal = UserPrincipal.of(user)
        val accessToken = accessTokenProvider.generateToken(userPrincipal)
        val refreshToken = refreshTokenHandler.issue(userPrincipal)

        return Pair(accessToken, refreshToken.payload)
    }

    data class LogoutCommand(
        val refreshTokenPayload: Token.Payload,
    )

    fun logout(command: LogoutCommand) {
        val (payload) = command
        val refreshToken = refreshTokenRepository.findByPayload(payload).orElseThrow {
            LogoutFailError
        }
        refreshTokenRepository.delete(refreshToken.id)
    }

    protected open fun authenticate(command: LoginCommand): BasicUser {
        val (username, password) = command
        val user = userRepository.findByUsername(username.value).orElseThrow {
            LoginFailError
        }
        val passwordVerification = passwordHashProvider.verify(password, user.passwordHash)
        if (!passwordVerification) {
            throw LoginFailError
        }
        if (!user.active) {
            throw UserDisabledError
        }
        return user
    }
}