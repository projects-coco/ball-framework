package org.coco.core.type

import arrow.core.Either

abstract class ValidType<V, A>(
    val unsafeMake: (V) -> (A),
) {
    abstract fun validate(value: V): Either<Reason, A>
}
