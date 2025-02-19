package org.coco.presentation.mvc.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.vo.PasswordWithoutValidation
import org.coco.domain.model.user.vo.Username
import org.coco.domain.service.auth.AuthTokenGenerator
import org.coco.domain.service.auth.Authenticator
import org.coco.domain.service.auth.RefreshTokenDeleter
import org.coco.presentation.mvc.core.*
import org.coco.presentation.mvc.middleware.BallAuthenticationToken
import org.springframework.context.annotation.Import
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@Import(
    Authenticator::class,
    AuthTokenGenerator::class,
    RefreshTokenDeleter::class,
)
class AuthController(
    private val authenticator: Authenticator,
    private val authTokenGenerator: AuthTokenGenerator,
    private val refreshTokenDeleter: RefreshTokenDeleter,
) {
    @GetMapping
    @IsAuthorized
    fun auth(ballAuthenticationToken: BallAuthenticationToken): ResponseEntity<UserPrincipal> =
        ResponseEntity
            .ok()
            .body(
                ballAuthenticationToken.userPrincipal,
            )

    data class LoginRequest(
        val username: String,
        val password: String,
    )

    @PostMapping("/login")
    @IsAnonymous
    fun login(
        @RequestBody request: LoginRequest,
        servletRequest: HttpServletRequest,
        servletResponse: HttpServletResponse,
    ): ResponseEntity<UserPrincipal> {
        val userPrincipal =
            authenticator.login(
                username = Username(request.username),
                password = PasswordWithoutValidation(request.password),
            )

        val (accessToken, refreshToken) = authTokenGenerator.generate(userPrincipal)
        servletResponse.sendAccessToken(accessToken)
        servletResponse.sendRefreshToken(refreshToken)

        return ResponseEntity
            .ok()
            .body(userPrincipal)
    }

    @PostMapping("/logout")
    fun logout(
        servletRequest: HttpServletRequest,
        servletResponse: HttpServletResponse,
    ): ResponseEntity<Unit> {
        runCatching {
            val refreshToken =
                servletRequest
                    .getRefreshToken()
                    .orElseThrow()
            refreshTokenDeleter.execute(refreshToken)
        }

        servletResponse.clearAccessToken()
        servletResponse.clearRefreshToken()

        return ResponseEntity
            .ok()
            .build()
    }
}
