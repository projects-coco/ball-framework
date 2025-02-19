package org.coco.domain.service.auth

import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.BasicUserRepository
import org.coco.domain.model.user.vo.IPassword
import org.coco.domain.model.user.vo.Username

class Authenticator(
    private val userRepository: BasicUserRepository<*, *>,
    private val passwordHashProvider: PasswordHashProvider,
) {
    companion object {
        val LoginFailError = LogicError("로그인에 실패했습니다. 아이디/비밀번호를 다시 확인해주세요.", ErrorType.UNAUTHORIZED)
        val UserDisabledError = LogicError("사용이 중지된 계정입니다. 관리자에게 문의해주세요.", ErrorType.UNAUTHORIZED)
    }

    fun login(
        username: Username,
        password: IPassword,
    ): UserPrincipal {
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

        userRepository.update(user.id) {
            it.loggingLogin()
        }

        return UserPrincipal.of(user)
    }
}
