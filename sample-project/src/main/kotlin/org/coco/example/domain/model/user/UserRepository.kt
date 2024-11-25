package org.coco.example.domain.model.user

import org.coco.domain.model.RepositoryBase
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface UserRepository : RepositoryBase<User>
