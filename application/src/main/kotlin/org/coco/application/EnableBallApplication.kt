package org.coco.application

import org.coco.application.lock.DistributedLockAspect
import org.coco.application.lock.LocalLockProvider
import org.coco.application.transaction.OptimisticLockRetryAspect
import org.coco.application.transaction.TxAdvice
import org.springframework.context.annotation.Import

@Import(
    TxAdvice::class,
    DistributedLockAspect::class,
    LocalLockProvider::class,
    OptimisticLockRetryAspect::class,
)
annotation class EnableBallApplication
