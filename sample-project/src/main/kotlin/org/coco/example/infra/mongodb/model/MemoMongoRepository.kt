package org.coco.example.infra.mongodb.model

import org.coco.infra.mongodb.helper.CustomMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemoMongoRepository : CustomMongoRepository<MemoDocumentModel> {
    fun findByTargetId(targetId: String): Optional<MemoDocumentModel>
}
