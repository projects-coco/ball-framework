package org.coco.example.application

import org.coco.core.extension.Logic
import org.coco.core.extension.bindOrThrow
import org.coco.core.extension.logic
import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class SampleService {
    fun sayHello(name: String): String {
        return considerToResponse(name).bindOrThrow {
            when (it) {
                ErrorOnConsider.Unlucky -> throw LogicError(
                    reason = "You're unlucky... $name",
                    errorType = ErrorType.INTERNAL_SERVER_ERROR
                )
            }
        }
    }

    sealed interface ErrorOnConsider {
        data object Unlucky : ErrorOnConsider
    }

    private fun considerToResponse(name: String): Logic<ErrorOnConsider, String> = logic {
        Random.nextInt(10).takeIf { it > 5 }?.let { "Hello, $name!" } ?: raise(ErrorOnConsider.Unlucky)
    }
}