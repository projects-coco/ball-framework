package org.coco.example.infra.mongodb

import org.coco.core.type.BinaryId
import org.coco.example.domain.model.memo.Memo
import org.coco.example.domain.model.memo.MemoRepository
import org.coco.example.infra.mongodb.model.MemoDocumentModel
import org.coco.example.infra.mongodb.model.MemoMongoRepository
import org.coco.infra.mongodb.helper.MongoRepositoryHelper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MemoRepositoryImpl(
    private val mongoRepository: MemoMongoRepository,
) : MongoRepositoryHelper<Memo, MemoDocumentModel>(mongoRepository, Memo::class),
    MemoRepository {
    override fun save(entity: Memo): Memo {
        val documentModel =
            MemoDocumentModel(
                entity.id.toString(),
                entity.targetId.toString(),
                entity.content,
                entity.createdAt,
                entity.updatedAt,
            )
        return mongoRepository.save(documentModel).toEntity()
    }

    override fun findByTargetId(targetId: BinaryId): Optional<Memo> =
        mongoRepository.findByTargetId(targetId.toString()).map { it.toEntity() }
}
