package org.coco.domain.model.auth

import org.coco.core.type.BinaryId
import org.coco.domain.model.user.BasicUser
import org.coco.domain.model.user.vo.Username
import java.security.Principal

data class UserPrincipal(
    val id: BinaryId,
    val username: Username,
    val roles: Set<String>,
) : Principal {
    override fun getName(): String = id.toString()

    companion object {
        fun of(user: BasicUser): UserPrincipal =
            UserPrincipal(
                id = user.id,
                username = user.username,
                roles = user.rolesAsString,
            )
    }
}
