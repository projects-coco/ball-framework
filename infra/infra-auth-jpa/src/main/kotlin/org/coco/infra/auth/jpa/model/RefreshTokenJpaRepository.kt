package org.coco.infra.auth.jpa.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenDataModel, ByteArray> {
    fun findByPayload(payload: String): Optional<RefreshTokenDataModel>
}