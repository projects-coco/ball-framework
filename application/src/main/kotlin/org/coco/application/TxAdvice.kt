package org.coco.application

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class TxAdvice {
    @Transactional
    fun <T> run(function: () -> T): T {
        return function.invoke()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> runWithNewTransaction(function: () -> T): T {
        return function.invoke()
    }
}
