package org.coco.example.infra.jpa

import org.coco.example.domain.model.user.User
import org.coco.example.domain.model.user.UserRepository
import org.coco.infra.jpa.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository :
    UserRepository,
    JpaRevisionRepository<User>
