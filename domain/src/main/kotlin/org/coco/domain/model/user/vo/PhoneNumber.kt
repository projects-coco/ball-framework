package org.coco.domain.model.user.vo

@JvmInline
value class PhoneNumber(
    val value: String,
) {
    init {
        val phoneNumberRegex = Regex("^[0-9]{3}-+[0-9]{3,4}-+[0-9]{4}\$")
        require(phoneNumberRegex.matches(value)) { "휴대폰 번호 양식을 확인해주세요." }
    }
}
