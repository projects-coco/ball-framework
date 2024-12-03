package org.coco.example.application

import arrow.core.toOption
import org.coco.domain.core.ErrorType
import org.coco.domain.core.LogicError
import org.coco.example.domain.model.user.User
import org.coco.example.domain.model.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun findUser(username: String): User {
        return userRepository.findByUsername(username)
            .orElseThrow { LogicError("$username not found", ErrorType.NOT_FOUND) }
    }

    fun createUser(username: String) {
        if (userRepository.findByUsername(username).isPresent) {
            throw LogicError("$username already exist", ErrorType.BAD_REQUEST)
        }
        val user = User(username = username)
        userRepository.save(user)
    }

    fun updateUsername(previous: String, newUsername: String) {
        val user = userRepository.findByUsername(previous)
            .orElseThrow { LogicError("$previous not found", ErrorType.NOT_FOUND) }
        runCatching {
            userRepository.update(user.id) {
                user.update(username = newUsername.toOption())
            }
        }.onFailure {
            throw it
        }
    }
}