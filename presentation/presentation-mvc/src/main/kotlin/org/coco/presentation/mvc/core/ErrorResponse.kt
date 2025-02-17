package org.coco.presentation.mvc.core

import org.coco.core.type.Reason

data class ErrorResponse(
    var error: Reason,
    var details: String? = null,
    var exception: Exception? = null,
)
