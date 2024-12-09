package org.coco.presentation.mvc.core

import org.coco.domain.core.Reason

data class ErrorResponse(
    var error: Reason? = null,
    var message: String? = null,
)