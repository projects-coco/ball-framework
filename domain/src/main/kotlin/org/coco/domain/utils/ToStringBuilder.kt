package org.coco.domain.utils

class ToStringBuilder(
    private val target: Any,
) {
    private val buffer: MutableList<Pair<String, Any?>> = mutableListOf()

    fun append(
        fieldName: String,
        obj: Any?,
    ): ToStringBuilder {
        this.buffer.addLast(Pair(fieldName, obj))
        return this
    }

    override fun toString(): String {
        val className = target::class.java.simpleName
        val value =
            buffer.joinToString(",") {
                "${it.first}=${it.second}"
            }
        return "$className($value)"
    }
}
