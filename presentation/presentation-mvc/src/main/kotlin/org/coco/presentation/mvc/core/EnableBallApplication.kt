package org.coco.presentation.mvc.core

import org.coco.application.TxAdvice
import org.coco.presentation.mvc.middleware.ErrorHandler
import org.coco.presentation.mvc.middleware.RequestLogger
import org.springframework.context.annotation.Import

@Import(
    ErrorHandler::class,
    RequestLogger::class,
    TxAdvice::class,
)
annotation class EnableBallApplication
