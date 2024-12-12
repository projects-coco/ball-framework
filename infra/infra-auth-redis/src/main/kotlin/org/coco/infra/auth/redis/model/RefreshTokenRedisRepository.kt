package org.coco.infra.auth.redis.model

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RefreshTokenRedisRepository : CrudRepository<RefreshTokenHashModel, String> {
    fun findByPayload(payload: String): Optional<RefreshTokenHashModel>
}
