package org.coco.domain.core

import arrow.core.Option
import arrow.core.identity
import arrow.core.raise.*
import kotlin.experimental.ExperimentalTypeInference

typealias Logic<E, A> = EagerEffect<E, A>
typealias SuspendLogic<E, A> = Effect<E, A>

@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalTypeInference::class)
inline fun <E, A> logic(@BuilderInference noinline block: Logic<E, A>): Logic<E, A> = block

@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalTypeInference::class)
inline fun <E, A> suspendLogic(@BuilderInference noinline block: SuspendLogic<E, A>): SuspendLogic<E, A> = block

context(Raise<E2>)
fun <E1, E2, A> Logic<E1, A>.bindOrRaise(transform: (E1) -> (E2)): A = mapError { transform(it) }.bind()

fun <E1, A> Logic<E1, A>.bindOrThrow(transform: (E1) -> (LogicError)): A = mapError { throw transform(it) }.get()

context(Raise<E2>)
suspend fun <E1, E2, A> SuspendLogic<E1, A>.bindOrRaise(transform: (E1) -> E2): A =
    fold({ raise(transform(it)) }, ::identity)

@Suppress("UnusedReceiverParameter")
fun <T> Nothing?.toOption(): Option<T> = Option.fromNullable(null)
