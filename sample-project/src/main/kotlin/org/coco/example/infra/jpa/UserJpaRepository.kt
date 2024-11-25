package org.coco.example.infra.jpa

import org.coco.domain.model.BinaryId
import org.coco.example.domain.model.user.User
import org.coco.example.domain.model.user.UserRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository :
    UserRepository,
    JpaRepository<User, BinaryId>
