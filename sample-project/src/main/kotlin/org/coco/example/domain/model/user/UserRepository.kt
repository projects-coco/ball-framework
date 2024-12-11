package org.coco.example.domain.model.user

import org.coco.domain.model.user.BasicUserRepository
import org.coco.domain.model.user.BasicUserSearchDto
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface UserRepository : BasicUserRepository<User, BasicUserSearchDto>
