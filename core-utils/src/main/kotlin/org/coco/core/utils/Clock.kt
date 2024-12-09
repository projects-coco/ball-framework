package org.coco.core.utils

import java.time.Clock
import java.time.ZoneId

val defaultTimeZone: ZoneId = ZoneId.of("Asia/Seoul") ?: ZoneId.systemDefault()

fun currentClock(): Clock = Clock.system(defaultTimeZone)
