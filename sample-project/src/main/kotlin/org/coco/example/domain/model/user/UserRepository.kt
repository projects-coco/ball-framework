package org.coco.example.domain.model.user

import org.coco.domain.model.RepositoryBase
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface UserRepository : RepositoryBase<User> {
    fun findByUsername(username: String): Optional<User>
}
