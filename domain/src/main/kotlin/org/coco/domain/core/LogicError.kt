package org.coco.domain.core

typealias Reason = String

data class LogicError(val reason: Reason) : RuntimeException(reason)
