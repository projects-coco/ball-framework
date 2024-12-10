package org.coco.domain.model.user

import arrow.core.Option
import org.coco.domain.core.toOption
import org.coco.domain.model.BinaryId
import org.coco.domain.model.SearchDto

open class BasicUserSearchDto(
    val id: Option<BinaryId> = null.toOption(),
    val username: Option<String> = null.toOption(),
    val name: Option<String> = null.toOption(),
    val phoneNumber: Option<String> = null.toOption(),
    val active: Option<Boolean> = null.toOption(),
    val loginCount: Option<Long> = null.toOption(),
): SearchDto