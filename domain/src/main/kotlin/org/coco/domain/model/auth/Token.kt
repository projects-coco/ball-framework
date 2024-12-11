package org.coco.domain.model.auth

data object Token {
    @JvmInline
    value class Payload(val value: String) {
        init {
            require(value.isNotBlank()) { "Payload must not be blank" }
        }
    }
}
