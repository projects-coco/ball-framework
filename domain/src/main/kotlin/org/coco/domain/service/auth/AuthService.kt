package org.coco.domain.service.auth

import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.domain.model.auth.RefreshTokenRepository
import org.coco.domain.model.auth.Token
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.BasicUser
import org.coco.domain.model.user.BasicUserRepository
import org.coco.domain.model.user.vo.Password
import org.coco.domain.model.user.vo.Username

open class AuthService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: BasicUserRepository<*, *>,
    private val passwordHashProvider: PasswordHashProvider,
) {
    companion object {
        val LoginFailError = LogicError("로그인에 실패했습니다. 아이디/비밀번호를 다시 확인해주세요.", ErrorType.UNAUTHORIZED)
        val UserDisabledError = LogicError("사용이 중지된 계정입니다. 관리자에게 문의해주세요.", ErrorType.UNAUTHORIZED)
        val LogoutFailError = LogicError("로그아웃 할 수 없습니다", ErrorType.BAD_REQUEST)
    }

    data class LoginCommand(
        val username: Username,
        val password: Password,
        val remoteIp: String,
    )

    fun login(command: LoginCommand): UserPrincipal {
        val user = authenticate(command)
        userRepository.update(user.id) {
            it.loggingLogin()
        }
        return UserPrincipal.of(user)
    }

    fun logout(refreshTokenPayload: Token.Payload) {
        val refreshToken =
            refreshTokenRepository.findByPayload(refreshTokenPayload).orElseThrow {
                LogoutFailError
            }
        refreshTokenRepository.delete(refreshToken.id)
    }

    protected open fun authenticate(command: LoginCommand): BasicUser {
        val (username, password) = command
        val user =
            userRepository.findByUsername(username.value).orElseThrow {
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
