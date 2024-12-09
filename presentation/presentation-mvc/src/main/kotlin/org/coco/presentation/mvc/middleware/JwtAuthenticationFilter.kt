package org.coco.presentation.mvc.middleware

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.coco.core.utils.ToStringBuilder
import org.coco.domain.model.auth.UserPrincipal
import org.coco.domain.service.auth.RefreshTokenHandler
import org.coco.domain.service.auth.TokenProvider
import org.coco.presentation.mvc.config.JwtConfig
import org.coco.presentation.mvc.core.getAccessToken
import org.coco.presentation.mvc.core.getRefreshToken
import org.coco.presentation.mvc.core.sendAccessToken
import org.coco.presentation.mvc.core.sendRefreshToken
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

data class BallAuthenticationToken(
    val userPrincipal: UserPrincipal,
) : AbstractAuthenticationToken(userPrincipal.roles.map { SimpleGrantedAuthority(it.toString()) }) {
    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Any = userPrincipal.username

    override fun getPrincipal(): Any = userPrincipal

    override fun toString(): String = ToStringBuilder(this)
        .append("userPrincipal", userPrincipal)
        .toString()
}

@Component
@Import(
    JwtConfig::class,
    RefreshTokenHandler::class
)
class JwtAuthenticationFilter(
    private val accessTokenProvider: TokenProvider,
    private val refreshTokenHandler: RefreshTokenHandler,
) : OncePerRequestFilter() {
    companion object {
        const val ACCESS_TOKEN_PREFIX = "Bearer "
        const val REFRESH_TOKEN_COOKIE_KEY = "refresh-token"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = request.getAccessToken()

        if (accessToken.isEmpty) {
            filterChain.doFilter(request, response)
            return
        }
        accessTokenProvider.verify(accessToken.get()).fold({
            when (it) {
                TokenProvider.VerifyError.InvalidToken -> {
                    filterChain.doFilter(request, response)
                    return
                }

                TokenProvider.VerifyError.Expired -> {
                    val refreshToken = request.getRefreshToken()
                    if (refreshToken.isEmpty) {
                        filterChain.doFilter(request, response)
                        return
                    }
                    runCatching {
                        val userPrincipal = refreshTokenHandler.consume(refreshToken.get())
                        val newAccessToken = accessTokenProvider.generateToken(userPrincipal)
                        val newRefreshToken = refreshTokenHandler.issue(userPrincipal)
                        response.sendAccessToken(newAccessToken)
                        response.sendRefreshToken(newRefreshToken.payload)
                        SecurityContextHolder.getContext().authentication =
                            BallAuthenticationToken(userPrincipal)
                    }
                }
            }
        }, { userPrincipal ->
            SecurityContextHolder.getContext().authentication = BallAuthenticationToken(userPrincipal)
        })
        filterChain.doFilter(request, response)
    }
}