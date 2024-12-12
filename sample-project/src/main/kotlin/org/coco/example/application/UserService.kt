package org.coco.example.application

import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.domain.model.user.BasicUser
import org.coco.domain.service.auth.PasswordHashProvider
import org.coco.example.domain.model.user.User
import org.coco.example.domain.model.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordHashProvider: PasswordHashProvider,
) {
    fun findUser(username: BasicUser.Username): User =
        userRepository
            .findByUsername(username.value)
            .orElseThrow { LogicError("$username not found", ErrorType.NOT_FOUND) }

    fun createUser(
        username: BasicUser.Username,
        password: User.Password,
    ) {
        if (userRepository.findByUsername(username.value).isPresent) {
            throw LogicError("$username already exist", ErrorType.BAD_REQUEST)
        }
        val passwordHash = passwordHashProvider.hash(password)
        val user =
            User(
                username = username,
                roles = setOf(User.Role.ROLE_USER),
                name = BasicUser.Name(username.value),
                phoneNumber = BasicUser.PhoneNumber("010-0000-0000"),
                passwordHash = passwordHash,
            )
        userRepository.save(user)
    }

    fun updateName(
        user: User,
        newName: BasicUser.Name,
    ) {
        userRepository.update(user) {
            it.updateName(newName)
        }
    }
}
