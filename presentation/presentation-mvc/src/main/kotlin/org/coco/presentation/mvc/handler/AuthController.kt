package org.coco.presentation.mvc.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.coco.domain.model.user.BasicUser
import org.coco.domain.service.auth.AuthService
import org.coco.presentation.mvc.core.getRemoteIp
import org.coco.presentation.mvc.core.sendAccessToken
import org.coco.presentation.mvc.core.sendRefreshToken
import org.springframework.context.annotation.Import
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
@Import(AuthService::class)
class AuthController(
    private val authService: AuthService,
) {
    data class LoginRequest(
        val username: String,
        val password: String,
    )

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    fun login(@RequestBody request: LoginRequest, servletRequest: HttpServletRequest, servletResponse: HttpServletResponse) {
        val tokenPair = authService.login(
            command = AuthService.LoginCommand(
                username = BasicUser.Username(request.username),
                password = BasicUser.Password(request.password),
                remoteIp = servletRequest.getRemoteIp(),
            )
        )
        servletResponse.sendAccessToken(tokenPair.first)
        servletResponse.sendRefreshToken(tokenPair.second)
    }
}