package org.coco.domain.core

import arrow.core.Either
import arrow.core.Option
import arrow.core.identity
import arrow.core.left

fun <T> Nothing?.toOption(): Option<T> = Option.fromNullable(null)
inline fun <E1, E2, A> Either<E1, A>.bindOrRaise(transform: (E1) -> E2) = fold({ (transform(it)).left() }, ::identity)