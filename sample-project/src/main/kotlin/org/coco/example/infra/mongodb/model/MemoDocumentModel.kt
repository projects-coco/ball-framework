package org.coco.example.infra.mongodb.model

import org.coco.example.domain.model.memo.Memo
import org.coco.infra.mongodb.model.DocumentModel
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "memos")
class MemoDocumentModel(
    entityId: String,
    targetId: String,
    content: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : DocumentModel<Memo>(entityId, createdAt, updatedAt) {
    var targetId: String = targetId
        private set

    var content: String = content
        private set

    override fun update(entity: Memo) {
        this.targetId = entity.targetId.toString()
        this.content = entity.content
        this.createdAt = entity.createdAt
        this.updatedAt = entity.updatedAt
    }
}
