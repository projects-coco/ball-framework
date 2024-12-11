package org.coco.domain.model.revision

import org.springframework.data.history.Revision
import org.springframework.data.history.RevisionMetadata
import java.time.Instant

data class BallRevisionDto<T>(
    val metadata: Metadata,
    val body: T,
) {
    companion object {
        data class Metadata(
            val revisionType: RevisionMetadata.RevisionType,
            val revisionNumber: Long,
            val revisionDate: Instant,
            val author: String?,
            val authorId: String?
        )

        fun <T> of(revision: Revision<Long, T>): BallRevisionDto<T> {
            val ballRevisionMetadata = revision.metadata.getDelegate<BallRevisionMetadata>()
            return BallRevisionDto(
                metadata = Metadata(
                    revisionType = revision.metadata.revisionType,
                    revisionNumber = ballRevisionMetadata.getRevisionNumber(),
                    revisionDate = ballRevisionMetadata.getRevisionInstant(),
                    author = ballRevisionMetadata.getAuthor()?.value,
                    authorId = ballRevisionMetadata.getAuthorId()?.toString(),
                ),
                body = revision.entity
            )
        }
    }
}