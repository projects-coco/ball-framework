package org.coco.domain.model.user.vo

@JvmInline
value class PasswordWithoutValidation(
    override val value: String,
) : IPassword
