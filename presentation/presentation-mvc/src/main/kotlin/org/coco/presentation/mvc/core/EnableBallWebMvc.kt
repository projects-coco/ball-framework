package org.coco.presentation.mvc.core

import org.coco.application.EnableBallApplication
import org.coco.application.transaction.TxAdvice
import org.coco.presentation.mvc.config.SecurityConfig
import org.coco.presentation.mvc.handler.AuthController
import org.coco.presentation.mvc.middleware.BallRequestFilter
import org.coco.presentation.mvc.middleware.BinaryIdSerializerConfig
import org.coco.presentation.mvc.middleware.ErrorHandler
import org.coco.presentation.mvc.middleware.RequestLogger
import org.springframework.context.annotation.Import

@EnableBallApplication
@Import(
    BallRequestFilter::class,
    ErrorHandler::class,
    RequestLogger::class,
    SecurityConfig::class,
    AuthController::class,
    TxAdvice::class,
    BinaryIdSerializerConfig::class,
)
annotation class EnableBallWebMvc
