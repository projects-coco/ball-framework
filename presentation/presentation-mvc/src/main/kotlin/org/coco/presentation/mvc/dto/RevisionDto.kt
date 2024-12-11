package org.coco.presentation.mvc.dto

import org.coco.core.utils.currentClock
import org.coco.domain.model.BallRevision
import org.springframework.data.history.Revision
import java.time.Instant

data class RevisionDto<T>(
    val metadata: Metadata,
    val body: T,
) {
    companion object {
        data class Metadata(
            val revisionNumber: Long,
            val revisionDate: Instant,
            val author: String?,
            val authorId: String?
        )
        fun <T> of(revision: Revision<Long, T>): RevisionDto<T> {
            val revisionMetadata = revision.metadata
            val ballRevision = revision.metadata.getDelegate<BallRevision>()
            return RevisionDto(
                metadata = Metadata(
                    revisionNumber = revisionMetadata.revisionNumber.orElse(0),
                    revisionDate = revisionMetadata.revisionInstant.orElse(Instant.now(currentClock())),
                    author = ballRevision.getAuthor()?.value,
                    authorId = ballRevision.getAuthorId()?.toString(),
                ),
                body = revision.entity)
        }
    }
}