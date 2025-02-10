package org.coco.domain.model.user.vo

@JvmInline
value class Password(
    override val value: String,
) : IPassword {
    init {
        val passwordComplexityRegex = Regex("^(?=.*?[a-zA-Z])(?=.*?[#?!@%^&*-]).{6,24}\$")
        require(passwordComplexityRegex.matches(value)) { "최소 하나 이상의 영문자, 특수문자(#?!@%^&*-)를 포함해 6자 이상 24자 이하의 패스워드를 입력해주세요." }
    }
}
