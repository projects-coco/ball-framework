package org.coco.example.application

import org.coco.domain.core.ErrorType
import org.coco.domain.core.LogicError
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
    fun findUser(username: String): User {
        return userRepository.findByUsername(username)
            .orElseThrow { LogicError("$username not found", ErrorType.NOT_FOUND) }
    }

    fun createUser(username: String) {
        if (userRepository.findByUsername(username).isPresent) {
            throw LogicError("$username already exist", ErrorType.BAD_REQUEST)
        }
        val passwordHash = passwordHashProvider.hash(BasicUser.Password(username))
        val user = User(
            username = BasicUser.Username(username),
            roles = setOf(User.Role.ROLE_USER),
            name = BasicUser.Name(username),
            phoneNumber = BasicUser.PhoneNumber("010-0000-0000"),
            passwordHash = passwordHash
        )
        userRepository.save(user)
    }

    fun updateUsername(previous: String, newUsername: String) {
        val user = userRepository.findByUsername(previous)
            .orElseThrow { LogicError("$previous not found", ErrorType.NOT_FOUND) }
        runCatching {
            userRepository.update(user.id) {
                it.updateUsername(BasicUser.Username(newUsername))
            }
        }.onFailure {
            throw it
        }
    }
}