package org.coco.example.application

import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.domain.model.user.vo.LegalName
import org.coco.domain.model.user.vo.Password
import org.coco.domain.model.user.vo.PhoneNumber
import org.coco.domain.model.user.vo.Username
import org.coco.domain.service.auth.PasswordHashProvider
import org.coco.example.domain.model.user.User
import org.coco.example.domain.model.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordHashProvider: PasswordHashProvider,
) {
    fun findUser(username: Username): User =
        userRepository
            .findByUsername(username.value)
            .orElseThrow { LogicError("$username not found", ErrorType.NOT_FOUND) }

    fun createUser(
        username: Username,
        password: Password,
    ) {
        if (userRepository.findByUsername(username.value).isPresent) {
            throw LogicError("$username already exist", ErrorType.BAD_REQUEST)
        }
        val passwordHash = passwordHashProvider.hash(password)
        val user =
            User(
                username = username,
                email = null,
                roles = setOf(User.Role.ROLE_USER),
                legalName = LegalName(username.value),
                phoneNumber = PhoneNumber("010-0000-0000"),
                passwordHash = passwordHash,
            )
        userRepository.save(user)
    }

    fun updateName(
        user: User,
        newLegalName: LegalName,
    ) {
        userRepository.update(user) {
            it.updateName(newLegalName)
        }
    }
}
