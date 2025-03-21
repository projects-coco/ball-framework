package org.coco.example.infra.jpa.model.user

import org.coco.infra.jpa.helper.JpaRevisionRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserJpaRepository : JpaRevisionRepository<UserDataModel> {
    fun findByUsername(username: String): Optional<UserDataModel>
}
