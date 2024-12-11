package org.coco.presentation.mvc.middleware

import jakarta.servlet.http.HttpServletRequest
import org.coco.core.type.BallRequestContext
import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.core.utils.logger
import org.coco.domain.model.auth.UserPrincipalContextHolder
import org.coco.presentation.mvc.core.ErrorResponse
import org.coco.presentation.mvc.core.getRemoteIp
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {
    private val logger = logger()

    private fun ErrorType.toHttpStatus(): HttpStatus = when (this) {
        ErrorType.BAD_REQUEST -> HttpStatus.BAD_REQUEST
        ErrorType.UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
        ErrorType.FORBIDDEN -> HttpStatus.FORBIDDEN
        ErrorType.NOT_FOUND -> HttpStatus.NOT_FOUND
        ErrorType.CONFLICT -> HttpStatus.CONFLICT
        ErrorType.INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
    }

    @ExceptionHandler(LogicError::class)
    fun handle(exception: LogicError): ResponseEntity<ErrorResponse> =
        exception.let {
            ResponseEntity
                .status(it.errorType.toHttpStatus())
                .body(
                    ErrorResponse(
                        error = it.reason,
                        message = it.message
                    )
                )
        }

    @ExceptionHandler(AuthorizationServiceException::class)
    fun handle(
        request: HttpServletRequest,
        exception: AuthorizationServiceException
    ): ResponseEntity<ErrorResponse> {
        logRequest(request)
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
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
    ): ResponseEntity<ErrorResponse> {
        logRequest(request)
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                ErrorResponse(
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
    ): ResponseEntity<ErrorResponse> {
        logger.debug(
            "# DEBUG | REQ_ID = {} | HANDLER = ErrorHandler | Exception:",
            BallRequestContext.requestId,
            exception
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    error =
                        "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.\n" +
                                "문제가 지속되면 관리자에게 문의해주세요.",
                    message = exception.message
                ),
            )
    }

    private fun logRequest(request: HttpServletRequest) {
        logger.warn(
            "# WARNING | REQ_ID = {} | METHOD = {} | PATH = {} | USERNAME = {} | REMOTE_ADDR = {}",
            BallRequestContext.requestId,
            request.method,
            request.requestURI,
            UserPrincipalContextHolder.userPrincipal?.username?.value ?: "<anonymous>",
            request.getRemoteIp(),
        )
    }
}