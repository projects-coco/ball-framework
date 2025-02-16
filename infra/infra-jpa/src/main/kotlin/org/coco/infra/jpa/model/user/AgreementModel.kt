package org.coco.infra.jpa.model.user

import jakarta.persistence.Embeddable
import org.coco.domain.model.user.vo.Agreement
import java.time.LocalDateTime

@Embeddable
class AgreementModel(
    status: Boolean,
    agreeAt: LocalDateTime,
) {
    var status: Boolean = status
        protected set
    var agreeAt: LocalDateTime = agreeAt
        protected set

    companion object {
        fun from(payload: Agreement): AgreementModel =
            AgreementModel(
                status = payload.status,
                agreeAt = payload.agreeAt,
            )
    }

    fun toDomain(): Agreement = Agreement(status, agreeAt)
}
