package org.coco.infra.jpa.core

import org.coco.domain.model.auth.UserPrincipalContextHolder
import org.coco.infra.jpa.model.BallRevisionEntity
import org.hibernate.envers.RevisionListener
import org.springframework.context.annotation.Configuration

@Configuration
class BallAuditRevisionListener : RevisionListener {
    override fun newRevision(revisionEntity: Any) {
        val ballRevisionEntity: BallRevisionEntity = revisionEntity as BallRevisionEntity
        UserPrincipalContextHolder.userPrincipal?.let {
            ballRevisionEntity.authorId = it.id.value
            ballRevisionEntity.author = it.username.value
        }
    }
}
