package org.coco.presentation.mvc.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.coco.domain.model.user.vo.Password
import org.coco.domain.model.user.vo.Username
import org.coco.domain.service.auth.AuthService
import org.coco.domain.service.auth.AuthTokenGenerator
import org.coco.presentation.mvc.core.*
import org.coco.presentation.mvc.middleware.BallAuthenticationToken
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
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
) {
    data class AuthResponse(
        val id: String,
        val roles: Set<String>,
        val username: String,
    )

    @GetMapping
    @IsAuthorized
    fun auth(ballAuthenticationToken: BallAuthenticationToken): ResponseEntity<AuthResponse> =
        ResponseEntity
            .status(HttpStatus.OK)
            .body(
                AuthResponse(
                    id = ballAuthenticationToken.userPrincipal.id.toString(),
                    roles =
                        ballAuthenticationToken.userPrincipal.roles
                            .map { it.toString() }
                            .toSet(),
                    username = ballAuthenticationToken.userPrincipal.username.value,
                ),
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
    ) {
        val tokenPair =
            authService.login(
                command =
                    AuthService.LoginCommand(
                        username = Username(request.username),
                        password = Password(request.password),
                        remoteIp = servletRequest.getRemoteIp(),
                    ),
            )
        servletResponse.sendAccessToken(tokenPair.first)
        servletResponse.sendRefreshToken(tokenPair.second)
    }
}
