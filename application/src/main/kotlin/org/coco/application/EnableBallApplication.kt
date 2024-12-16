package org.coco.application

import org.coco.application.lock.DistributedLockAop
import org.coco.application.lock.LocalLockProvider
import org.coco.application.transaction.TxAdvice
import org.springframework.context.annotation.Import

@Import(
    TxAdvice::class,
    DistributedLockAop::class,
    LocalLockProvider::class,
)
annotation class EnableBallApplication
