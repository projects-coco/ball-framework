package org.coco.domain.model

import org.coco.domain.core.ErrorType
import org.coco.domain.core.LogicError
import kotlin.reflect.KClass

class EntityNotFoundError(kClass: KClass<*>, id: BinaryId) :
    LogicError("${kClass.simpleName}(id=$id, hex=${id.toHexString()}) is not found.", ErrorType.NOT_FOUND)