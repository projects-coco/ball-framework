package org.coco.domain.core

import arrow.core.Option
import arrow.core.raise.Raise
import arrow.core.raise.mapError
import kotlin.experimental.ExperimentalTypeInference

typealias Logic<E, A> = Raise<E>.() -> A

@OptIn(ExperimentalTypeInference::class)
fun <E, A> logic(@BuilderInference block: Logic<E, A>): Logic<E, A> = block

context(Raise<E2>)
fun <E1, E2, A> Logic<E1, A>.bindOrRaise(transform: (E1) -> (E2)): A = mapError { transform(it) }.bind()

context(Raise<Nothing>)
fun <E1, A> Logic<E1, A>.bindOrThrow(transform: (E1) -> (LogicError)): A = mapError { throw transform(it) }.bind()

@Suppress("UnusedReceiverParameter")
fun <T> Nothing?.toOption(): Option<T> = Option.fromNullable(null)
