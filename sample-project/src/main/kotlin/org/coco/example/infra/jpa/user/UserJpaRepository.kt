package org.coco.example.infra.jpa.user

import org.coco.infra.jpa.JpaRevisionRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserJpaRepository : JpaRevisionRepository<UserDataModel> {
    fun findByUsername(username: String): Optional<UserDataModel>
}
