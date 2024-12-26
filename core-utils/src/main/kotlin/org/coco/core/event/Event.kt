package org.coco.core.event

import org.coco.core.utils.currentTimestamp

abstract class Event(
    open val timestamp: Long = currentTimestamp(),
)
