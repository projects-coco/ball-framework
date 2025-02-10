package org.coco.domain.model.user.vo

@JvmInline
value class LegalName(
    val value: String,
) {
    init {
        val nameMinLength = 2
        require(value.length >= nameMinLength) { "이름은 최소 ${nameMinLength}자 이상의 값이 필요합니다." }
    }
}
