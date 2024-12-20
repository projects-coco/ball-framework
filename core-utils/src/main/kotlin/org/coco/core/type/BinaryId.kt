package org.coco.core.type

import ulid.ULID

@JvmInline
value class BinaryId(val value: ByteArray) : Comparable<BinaryId> {
    companion object {
        fun new(): BinaryId = BinaryId(ULID.nextULID().toBytes())

        fun fromString(value: String): BinaryId = BinaryId(ULID.parseULID(value).toBytes())
    }

    init {
        runCatching { ULID.fromBytes(value).toBytes() }.getOrNull()
            ?: throw LogicError("Invalid ULID payload")
    }

    override fun compareTo(other: BinaryId): Int = compareValuesBy(this, other)

    override fun toString(): String {
        return ULID.fromBytes(value).toString()
    }

    fun toHexString(): String = "0x${this.value.joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }}"
}
