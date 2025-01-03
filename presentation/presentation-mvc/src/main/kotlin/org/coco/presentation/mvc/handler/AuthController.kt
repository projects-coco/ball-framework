package org.coco.presentation.mvc.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.coco.domain.model.user.BasicUser
import org.coco.domain.service.auth.AuthService
import org.coco.presentation.mvc.core.*
import org.coco.presentation.mvc.middleware.BallAuthenticationToken
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@Import(AuthService::class)
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
    fun auth(ballAuthenticationToken: BallAuthenticationToken): ResponseEntity<AuthResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                AuthResponse(
                    id = ballAuthenticationToken.userPrincipal.id.toString(),
                    roles = ballAuthenticationToken.userPrincipal.roles.map { it.toString() }.toSet(),
                    username = ballAuthenticationToken.userPrincipal.username.value,
                ),
            )
    }

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
                        username = BasicUser.Username(request.username),
                        password = BasicUser.Password(request.password),
                        remoteIp = servletRequest.getRemoteIp(),
                    ),
            )
        servletResponse.sendAccessToken(tokenPair.first)
        servletResponse.sendRefreshToken(tokenPair.second)
    }
}
