package org.coco.domain.core

import arrow.core.Either

typealias Reason = String

abstract class ValidType<V, A>(
    val unsafeMake: (V) -> (A),
) {
    abstract fun validate(value: V): Either<Reason, A>
}