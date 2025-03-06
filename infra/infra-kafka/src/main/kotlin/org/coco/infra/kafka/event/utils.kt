@file:Suppress("ktlint:standard:filename")

package org.coco.infra.kafka.event

import org.coco.core.event.Event
import kotlin.reflect.KClass

fun Event.topic(): String = "ball.event.${this::class.simpleName!!}"

fun KClass<*>.topic(): String = "ball.event.${this.simpleName!!}"
