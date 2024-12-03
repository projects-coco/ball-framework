package org.coco.domain.core

typealias Reason = String

enum class ErrorType {
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    CONFLICT,
    INTERNAL_SERVER_ERROR,
    ;
}

open class LogicError(
    val reason: Reason,
    val errorType: ErrorType = ErrorType.INTERNAL_SERVER_ERROR,
) : RuntimeException(reason)
