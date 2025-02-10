package org.coco.domain.model.user.vo

@JvmInline
value class Username(
    val value: String,
) {
    init {
        val usernameMinLength = 5
        require(value.length >= usernameMinLength) { "아이디는 최소 ${usernameMinLength}자 이상의 값이 필요합니다." }
    }
}
