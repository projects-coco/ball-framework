package org.coco.domain.model.user

import org.coco.domain.model.RepositoryBase
import java.util.*

interface BasicUserRepository<T : BasicUser> : RepositoryBase<T> {
    fun findByUsername(username: String): Optional<T>
}