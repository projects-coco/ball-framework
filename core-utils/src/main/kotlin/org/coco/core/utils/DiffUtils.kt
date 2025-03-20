package org.coco.core.utils

import org.javers.core.JaversBuilder
import org.javers.core.diff.Diff

object DiffUtils {
    private val javers = JaversBuilder.javers().build()

    fun <T> diff(
        left: T,
        right: T,
    ): Diff = javers.compare(left, right)
}
