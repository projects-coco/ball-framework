package org.coco.domain.exception

import org.coco.core.type.BinaryId
import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import kotlin.reflect.KClass

class EntityNotFoundError(kClass: KClass<*>, id: BinaryId) :
    LogicError("${kClass.simpleName}(id=$id, hex=${id.toHexString()}) is not found.", ErrorType.NOT_FOUND)
