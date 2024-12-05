package org.coco.presentation.mvc.config

import org.coco.presentation.mvc.middleware.BallAccessDeniedHandler
import org.coco.presentation.mvc.middleware.BallAuthenticationEntryPoint
import org.coco.presentation.mvc.middleware.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Import(
    BallAccessDeniedHandler::class,
    BallAuthenticationEntryPoint::class,
    JwtAuthenticationFilter::class,
)
class SecurityConfig(
    private val accessDeniedHandler: AccessDeniedHandler,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { Customizer { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() } }
            .sessionManagement { configurer: SessionManagementConfigurer<HttpSecurity> ->
                configurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .formLogin { obj: FormLoginConfigurer<HttpSecurity> -> obj.disable() }
            .httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { configure: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
                configure.anyRequest().permitAll()
            }
            .exceptionHandling { configurer: ExceptionHandlingConfigurer<HttpSecurity> ->
                configurer.authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            }
            .build()
    }
}