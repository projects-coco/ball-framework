package org.coco.presentation.mvc.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.model.user.vo.Password
import org.coco.domain.model.user.vo.Username
import org.coco.domain.service.auth.AuthService
import org.coco.domain.service.auth.AuthService.Companion.LoginFailError
import org.coco.domain.service.auth.AuthTokenGenerator
import org.coco.presentation.mvc.core.*
import org.coco.presentation.mvc.middleware.BallAuthenticationToken
import org.springframework.context.annotation.Import
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@Import(
    AuthService::class,
    AuthTokenGenerator::class,
)
class AuthController(
    private val authService: AuthService,
    private val authTokenGenerator: AuthTokenGenerator,
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
    ): ResponseEntity<UserPrincipal> =
        runCatching {
            val userPrincipal =
                authService.login(
                    command =
                        AuthService.LoginCommand(
                            username = Username(request.username),
                            password = Password(request.password),
                            remoteIp = servletRequest.getRemoteIp(),
                        ),
                )

            val (accessToken, refreshToken) = authTokenGenerator.generate(userPrincipal)
            servletResponse.sendAccessToken(accessToken)
            servletResponse.sendRefreshToken(refreshToken)

            ResponseEntity
                .ok()
                .body(userPrincipal)
        }.getOrElse {
            throw LoginFailError
        }

    @PostMapping("/logout")
    @IsAuthorized
    fun logout(
        servletRequest: HttpServletRequest,
        servletResponse: HttpServletResponse,
    ): ResponseEntity<Unit> {
        val refreshToken =
            servletRequest
                .getRefreshToken()
                .orElseThrow()
        authService.logout(refreshToken)

        servletResponse.clearAccessToken()
        servletResponse.clearRefreshToken()

        return ResponseEntity
            .ok()
            .build()
    }
}
