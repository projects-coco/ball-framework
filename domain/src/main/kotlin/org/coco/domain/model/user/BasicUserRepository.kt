package org.coco.domain.model.user

import org.coco.domain.model.RepositoryBase
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface BasicUserRepository<T : BasicUser> : RepositoryBase<T> {
    fun findByUsername(username: String): Optional<T>
}