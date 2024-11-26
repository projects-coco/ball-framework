package org.coco.example.application

import arrow.core.raise.fold
import arrow.core.toOption
import org.coco.domain.core.Logic
import org.coco.domain.core.bindOrRaise
import org.coco.domain.core.logic
import org.coco.domain.model.toLogic
import org.coco.example.domain.model.user.User
import org.coco.example.domain.model.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {
    sealed interface CreateUserError {
        data class AlreadyExist(val username: String) : CreateUserError
        data object CrudError : CreateUserError
    }

    fun createUser(username: String): Logic<CreateUserError, User> = logic {
        userRepository.findByUsername(username).toLogic()
            .fold(
                {
                    val user = User(username = username)
                    userRepository.save(user)
                },
                { raise(CreateUserError.AlreadyExist(username)) },
            )
    }

    sealed interface UpdateUserError {
        data object NotFound : UpdateUserError
        data object CrudError : UpdateUserError
    }

    fun updateUsername(previous: String, newUsername: String): Logic<UpdateUserError, Unit> = logic {
        val user = userRepository.findByUsername(previous).toLogic()
            .bindOrRaise { raise(UpdateUserError.NotFound) }
        runCatching {
            user.update(username = newUsername.toOption())
        }.onFailure {
            raise(UpdateUserError.CrudError)
        }
    }
}