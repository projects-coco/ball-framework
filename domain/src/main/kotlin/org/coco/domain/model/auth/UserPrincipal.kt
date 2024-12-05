package org.coco.domain.model.auth

import org.coco.domain.model.BinaryId
import org.coco.domain.model.user.BasicUser
import java.security.Principal

data class UserPrincipal(
    val id: BinaryId,
    val roles: Set<BasicUser.IRole>,
) : Principal {
    override fun getName(): String = id.toString()
}