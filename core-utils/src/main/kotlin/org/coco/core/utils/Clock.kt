package org.coco.core.utils

import java.time.Clock
import java.time.OffsetDateTime
import java.time.ZoneId

val defaultTimeZone: ZoneId = ZoneId.of("Asia/Seoul") ?: ZoneId.systemDefault()

fun currentClock(): Clock = Clock.system(defaultTimeZone)

fun currentTimestamp(zoneId: ZoneId = defaultTimeZone): Long {
    val now = OffsetDateTime.now(zoneId)
    return now.toInstant().toEpochMilli()
}
