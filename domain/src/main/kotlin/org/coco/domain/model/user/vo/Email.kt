package org.coco.domain.model.user.vo

@JvmInline
value class Email(
    val value: String,
) {
    init {
        require(value.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) {
            "잘못된 이메일 형식입니다: $value"
        }
    }
}
