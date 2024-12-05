package org.coco.domain.model.auth

import org.coco.domain.model.RepositoryBase
import org.coco.domain.model.auth.Token.Payload
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface RefreshTokenRepository : RepositoryBase<RefreshToken> {
    fun findByPayload(payload: Payload): Optional<RefreshToken>
}