package org.coco.presentation.mvc.core

import arrow.core.Either
import arrow.core.Option
import arrow.core.raise.Raise
import arrow.core.toOption
import org.coco.domain.core.Reason
import org.coco.domain.core.ValidType
import org.springframework.http.HttpStatus
import kotlin.reflect.KProperty0

sealed class ApiError(
    val status: HttpStatus,
    val body: Body,
) {
    data class ApiException(
        val error: ApiError,
    ) : RuntimeException()

    data class Body(
        var error: Reason? = null,
        var exception: Exception? = null,
    )

    data class ValidationFailed(
        val error: Reason,
    ) : ApiError(
        HttpStatus.BAD_REQUEST,
        Body(error),
    )

    data class BadRequest(
        val error: Reason,
    ) : ApiError(HttpStatus.BAD_REQUEST, Body(error = error))

    data object Unauthorized : ApiError(HttpStatus.UNAUTHORIZED, Body())

    data class Forbidden(
        val error: Reason,
    ) : ApiError(HttpStatus.FORBIDDEN, Body(error = error))

    data class NotFound(
        val error: Reason,
    ) : ApiError(HttpStatus.NOT_FOUND, Body(error = error))

    data class InternalServerError(
        val error: Reason,
    ) : ApiError(HttpStatus.INTERNAL_SERVER_ERROR, Body(error = error))
}

context(Raise<ApiError>)
fun <A> Either<String, A>.bindOrFail(): A = mapLeft { ApiError.ValidationFailed(it) }.bind()

context(Raise<ApiError>)
fun <V, A> KProperty0<V>.validate(validType: ValidType<V, A>): A = validType.validate(invoke()).bindOrFail()

context(Raise<ApiError>)
fun <V, A> KProperty0<V?>.validateToOption(validType: ValidType<V, A>): Option<A> =
    invoke().toOption().map { validType.validate(it).bindOrFail() }

context(Raise<ApiError>)
fun raiseBadRequest(error: Reason): Nothing = raise(ApiError.BadRequest(error))

context(Raise<ApiError>)
fun raiseNotFound(error: Reason): Nothing = raise(ApiError.NotFound(error))

context(Raise<ApiError>)
fun raiseForbidden(error: Reason): Nothing = raise(ApiError.Forbidden(error))

context(Raise<ApiError>)
fun raiseInternalServerError(error: Reason): Nothing = raise(ApiError.InternalServerError(error))