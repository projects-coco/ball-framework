package org.coco.presentation.mvc.core

import org.coco.core.type.Reason

data class ErrorResponse(
    var error: Reason? = null,
    var message: String? = null,
)
