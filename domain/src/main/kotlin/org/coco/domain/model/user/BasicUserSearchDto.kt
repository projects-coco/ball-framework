package org.coco.domain.model.user

import arrow.core.Option
import org.coco.core.extension.toOption
import org.coco.core.type.BinaryId
import org.coco.domain.model.SearchDto

open class BasicUserSearchDto(
    open val id: Option<BinaryId> = null.toOption(),
    open val username: Option<String> = null.toOption(),
    open val name: Option<String> = null.toOption(),
    open val phoneNumber: Option<String> = null.toOption(),
    open val active: Option<Boolean> = null.toOption(),
    open val loginCount: Option<Long> = null.toOption(),
) : SearchDto
