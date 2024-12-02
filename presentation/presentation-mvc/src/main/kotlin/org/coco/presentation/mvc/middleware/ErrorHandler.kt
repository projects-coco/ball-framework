package org.coco.presentation.mvc.middleware

import jakarta.servlet.http.HttpServletRequest
import org.coco.domain.core.ErrorType
import org.coco.domain.core.LogicError
import org.coco.presentation.mvc.core.ApiError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {
    private fun ErrorType.toHttpStatus(): HttpStatus = when (this) {
        ErrorType.BAD_REQUEST -> HttpStatus.BAD_REQUEST
        ErrorType.UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
        ErrorType.FORBIDDEN -> HttpStatus.FORBIDDEN
        ErrorType.NOT_FOUND -> HttpStatus.NOT_FOUND
        ErrorType.CONFLICT -> HttpStatus.CONFLICT
        ErrorType.INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
    }

    @ExceptionHandler
    fun handle(exception: LogicError): ResponseEntity<ApiError.Body> =
        exception.let {
            ResponseEntity
                .status(it.errorType.toHttpStatus())
                .body(
                    ApiError.Body(
                        error = it.reason,
                        exception = it
                    )
                )
        }

    @ExceptionHandler
    fun handle(exception: ApiError.ApiException): ResponseEntity<ApiError.Body> =
        exception.error.let {
            ResponseEntity
                .status(it.status)
                .body(it.body)
        }

    @ExceptionHandler
    fun handle(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<ApiError.Body> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiError.Body(
                    error =
                        "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.\n" +
                                "문제가 지속되면 관리자에게 문의해주세요.",
                    exception = exception,
                ),
            )
    }
}