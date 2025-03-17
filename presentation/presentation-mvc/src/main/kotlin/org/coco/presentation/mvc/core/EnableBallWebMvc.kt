package org.coco.presentation.mvc.core

import org.coco.application.EnableBallApplication
import org.coco.presentation.mvc.config.JwtConfig
import org.coco.presentation.mvc.config.SecurityConfig
import org.coco.presentation.mvc.handler.AuthController
import org.coco.presentation.mvc.middleware.BallRequestFilter
import org.coco.presentation.mvc.middleware.ErrorHandler
import org.coco.presentation.mvc.middleware.JwtAuthenticationFilter
import org.coco.presentation.mvc.middleware.RequestLogger
import org.springframework.context.annotation.Import

@EnableBallApplication
@Import(
    // Config
    JwtConfig::class,
    SecurityConfig::class,
    // middleware
    BallRequestFilter::class,
    ErrorHandler::class,
    JwtAuthenticationFilter::class,
    RequestLogger::class,
    // handler
    AuthController::class,
)
annotation class EnableBallWebMvc
