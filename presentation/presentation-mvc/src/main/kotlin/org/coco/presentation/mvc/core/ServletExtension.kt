package org.coco.presentation.mvc.core

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.coco.domain.model.auth.Token
import org.coco.presentation.mvc.middleware.JwtAuthenticationFilter.Companion.ACCESS_TOKEN_PREFIX
import org.coco.presentation.mvc.middleware.JwtAuthenticationFilter.Companion.REFRESH_TOKEN_COOKIE_KEY
import org.springframework.http.HttpHeaders
import java.time.Duration
import java.util.*

fun HttpServletRequest.getRemoteIp(): String = getHeader("X-Forwarded-For") ?: remoteAddr

fun HttpServletRequest.getAccessToken(): Optional<Token.Payload> =
    Optional
        .ofNullable(this.getHeader(HttpHeaders.AUTHORIZATION)?.removePrefix(ACCESS_TOKEN_PREFIX))
        .map { Token.Payload(it) }

fun HttpServletRequest.getRefreshToken(): Optional<Token.Payload> =
    Optional
        .ofNullable(
            this.cookies?.let { cookies ->
                cookies.find { it.name == REFRESH_TOKEN_COOKIE_KEY }
            },
        ).map { Token.Payload(it.value) }

fun HttpServletResponse.sendAccessToken(payload: Token.Payload) {
    this.addHeader(HttpHeaders.AUTHORIZATION, payload.value)
}

fun HttpServletResponse.clearAccessToken() {
    this.addHeader(HttpHeaders.AUTHORIZATION, null)
}

fun HttpServletResponse.sendRefreshToken(payload: Token.Payload) {
    val refreshTokenCookie = Cookie(REFRESH_TOKEN_COOKIE_KEY, payload.value)
    refreshTokenCookie.maxAge = Duration.ofDays(14).toSeconds().toInt()
    refreshTokenCookie.path = "/"
    refreshTokenCookie.isHttpOnly = true
    refreshTokenCookie.secure = true
    this.addCookie(
        refreshTokenCookie,
    )
}

fun HttpServletResponse.clearRefreshToken() {
    val refreshTokenCookie = Cookie(REFRESH_TOKEN_COOKIE_KEY, null)
    refreshTokenCookie.maxAge = 0
    refreshTokenCookie.path = "/"
    refreshTokenCookie.isHttpOnly = true
    refreshTokenCookie.secure = true
}
