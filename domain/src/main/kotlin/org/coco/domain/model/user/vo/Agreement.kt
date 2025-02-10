package org.coco.domain.model.user.vo

import org.coco.core.utils.ToStringBuilder
import org.coco.core.utils.currentClock
import java.time.LocalDateTime

open class Agreement(
    status: Boolean,
    agreeAt: LocalDateTime,
) {
    @Suppress("unused")
    constructor() : this(
        false,
        LocalDateTime.now(currentClock()),
    )

    open var status: Boolean = status
        protected set

    open var agreeAt: LocalDateTime = agreeAt
        protected set

    companion object {
        fun agree() = Agreement(true, LocalDateTime.now(currentClock()))

        fun disagree() = Agreement(false, LocalDateTime.now(currentClock()))
    }

    override fun toString(): String =
        ToStringBuilder(this)
            .append("status", status)
            .append("agreeAt", agreeAt)
            .toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Agreement
        if (status != other.status) return false
        if (agreeAt != other.agreeAt) return false
        return true
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
