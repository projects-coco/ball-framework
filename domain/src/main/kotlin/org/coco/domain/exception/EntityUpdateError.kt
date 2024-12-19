package org.coco.domain.exception

import org.coco.core.type.BinaryId
import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import kotlin.reflect.KClass

class EntityUpdateError(
    kClass: KClass<*>,
    id: BinaryId,
) : LogicError(
        "Error occurred when update ${kClass.simpleName}(id=$id, hex=${id.toHexString()}).",
        ErrorType.INTERNAL_SERVER_ERROR,
    )
