package org.coco.application

import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
class TxAdvice {
    @Transactional
    fun <T> run(function: () -> T): T {
        return function.invoke()
    }
}