package org.coco.example.infra.mongodb

import org.coco.core.type.BinaryId
import org.coco.example.domain.model.memo.Memo
import org.coco.example.domain.model.memo.MemoRepository
import org.coco.example.domain.model.user.UserRepository
import org.coco.example.infra.mongodb.model.MemoDocumentModel
import org.coco.example.infra.mongodb.model.MemoMongoRepository
import org.coco.infra.mongodb.helper.MongoRepositoryHelper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MemoRepositoryImpl(
    private val mongoRepository: MemoMongoRepository,
    private val userRepository: UserRepository,
) : MongoRepositoryHelper<Memo, MemoDocumentModel>(mongoRepository, Memo::class),
    MemoRepository {
    override fun MemoDocumentModel.toEntity(): Memo {
        val writer = userRepository.findById(BinaryId.fromString(writerId)).orElseThrow()
        return Memo(
            id = BinaryId.fromString(entityId),
            targetId = BinaryId.fromString(targetId),
            writer = writer,
            content = content,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    override fun Memo.toModel(): MemoDocumentModel =
        MemoDocumentModel(
            entityId = id.toString(),
            targetId = targetId.toString(),
            writerId = writer.id.toString(),
            content = content,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    override fun findByTargetId(targetId: BinaryId): Optional<Memo> =
        mongoRepository.findByTargetId(targetId.toString()).map { it.toEntity() }
}
