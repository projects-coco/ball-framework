package org.coco.domain.model.user.vo

import org.coco.core.utils.currentClock
import java.time.LocalDateTime

data class Agreement(
    val status: Boolean,
    val agreeAt: LocalDateTime,
) {
    companion object {
        fun agree() = Agreement(true, LocalDateTime.now(currentClock()))

        fun disagree() = Agreement(false, LocalDateTime.now(currentClock()))
    }
}
