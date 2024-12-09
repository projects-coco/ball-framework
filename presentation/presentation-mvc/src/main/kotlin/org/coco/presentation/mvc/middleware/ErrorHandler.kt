package org.coco.presentation.mvc.middleware

import jakarta.servlet.http.HttpServletRequest
import org.coco.core.utils.logger
import org.coco.domain.core.ErrorType
import org.coco.domain.core.LogicError
import org.coco.presentation.mvc.core.ApiError
import org.coco.presentation.mvc.core.getRemoteIp
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice(annotations = [RestController::class])
class ErrorHandler : ResponseEntityExceptionHandler() {
    private val log = logger()

    private fun ErrorType.toHttpStatus(): HttpStatus = when (this) {
        ErrorType.BAD_REQUEST -> HttpStatus.BAD_REQUEST
        ErrorType.UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
        ErrorType.FORBIDDEN -> HttpStatus.FORBIDDEN
        ErrorType.NOT_FOUND -> HttpStatus.NOT_FOUND
        ErrorType.CONFLICT -> HttpStatus.CONFLICT
        ErrorType.INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
    }

    @ExceptionHandler(LogicError::class)
    fun handle(exception: LogicError): ResponseEntity<ApiError.Body> =
        exception.let {
            ResponseEntity
                .status(it.errorType.toHttpStatus())
                .body(
                    ApiError.Body(
                        error = it.reason,
                        message = it.message
                    )
                )
        }

    @ExceptionHandler(ApiError.ApiException::class)
    fun handle(exception: ApiError.ApiException): ResponseEntity<ApiError.Body> =
        exception.error.let {
            ResponseEntity
                .status(it.status)
                .body(it.body)
        }

    @ExceptionHandler(AuthorizationServiceException::class)
    fun handle(
        request: HttpServletRequest,
        exception: AuthorizationServiceException
    ): ResponseEntity<ApiError.Body> {
        logRequest(request)
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ApiError.Body(
                    error =
                        "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.\n" +
                                "문제가 지속되면 관리자에게 문의해주세요.",
                ),
            )
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handle(
        request: HttpServletRequest,
        exception: AccessDeniedException
    ): ResponseEntity<ApiError.Body> {
        logRequest(request)
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                ApiError.Body(
                    error =
                        "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.\n" +
                                "문제가 지속되면 관리자에게 문의해주세요.",
                ),
            )
    }

    @ExceptionHandler(Exception::class)
    fun handle(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<ApiError.Body> {
        log.debug("# DEBUG | REQ_ID = ${request.requestId} | HANDLER = ErrorHandler | Exception:", exception)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiError.Body(
                    error =
                        "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.\n" +
                                "문제가 지속되면 관리자에게 문의해주세요.",
                    message = exception.message
                ),
            )
    }

    private fun logRequest(request: HttpServletRequest) {
        log.warn(
            "# ERROR | REQ_ID = {} | METHOD = {} | PATH = {} | REMOTE_ADDR = {}",
            request.requestId,
            request.method,
            request.requestURI,
            request.getRemoteIp(),
        )
    }
}