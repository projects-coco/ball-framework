package org.coco.domain.model.user

import org.coco.domain.model.RepositoryBase
import org.coco.domain.model.SearchRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface BasicUserRepository<T : BasicUser, S : BasicUserSearchDto> : RepositoryBase<T>, SearchRepository<T, S> {
    fun findByUsername(username: String): Optional<T>
}