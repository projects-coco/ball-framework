package org.coco.presentation.mvc.core

import org.coco.presentation.mvc.config.SecurityConfig
import org.coco.presentation.mvc.handler.AuthController
import org.coco.presentation.mvc.middleware.ErrorHandler
import org.coco.presentation.mvc.middleware.RequestLogger
import org.springframework.context.annotation.Import

@Import(
    ErrorHandler::class,
    RequestLogger::class,
    SecurityConfig::class,
    AuthController::class,
)
annotation class EnableBallApplication
