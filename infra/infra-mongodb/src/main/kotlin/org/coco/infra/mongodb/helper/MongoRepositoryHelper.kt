package org.coco.infra.mongodb.helper

import org.coco.core.type.BinaryId
import org.coco.core.type.LogicError
import org.coco.domain.exception.EntityNotFoundError
import org.coco.domain.model.EntityBase
import org.coco.domain.model.RepositoryBase
import org.coco.infra.mongodb.model.DocumentModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.history.Revision
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.reflect.KClass

@Transactional
abstract class MongoRepositoryHelper<E : EntityBase, D : DocumentModel<E>>(
    private val mongoRepository: CustomMongoRepository<D>,
    private val entityClass: KClass<E>,
) : RepositoryBase<E> {
    companion object {
        val RevisionNotImplementedError = LogicError("Revision 을 찾을 수 없습니다.")
    }

    abstract fun D.toEntity(): E

    abstract fun E.toModel(): D

    fun modelToEntity(documentModel: D): E = documentModel.toEntity()

    override fun findById(id: BinaryId): Optional<E> = mongoRepository.findByEntityId(id.toString()).map { it.toEntity() }

    override fun findAll(): List<E> = mongoRepository.findAll().map { it.toEntity() }

    override fun findAll(ids: List<BinaryId>): List<E> = mongoRepository.findAllByEntityId(ids.map { it.toString() }).map { it.toEntity() }

    override fun findAll(pageable: Pageable): Page<E> = mongoRepository.findAll(pageable).map { it.toEntity() }

    override fun save(entity: E): E {
        val documentModel = entity.toModel()
        return mongoRepository.save(documentModel).toEntity()
    }

    override fun update(
        id: BinaryId,
        modifier: (E) -> Unit,
    ) {
        val documentModel =
            mongoRepository.findByEntityId(id.toString()).orElseThrow { EntityNotFoundError(entityClass, id) }
        val entity = documentModel.toEntity()
        modifier.invoke(entity)
        documentModel.update(entity)
        mongoRepository.save(documentModel)
    }

    override fun delete(id: BinaryId) {
        val documentModel =
            mongoRepository.findByEntityId(id.toString()).orElseThrow { EntityNotFoundError(entityClass, id) }
        mongoRepository.deleteById(documentModel.id)
    }

    override fun findRevisions(id: BinaryId): List<Revision<Long, E>> = throw RevisionNotImplementedError

    override fun findRevisions(
        id: BinaryId,
        pageable: Pageable,
    ): Page<Revision<Long, E>> = throw RevisionNotImplementedError

    override fun findRevision(
        id: BinaryId,
        revisionNumber: Long,
    ): Optional<Revision<Long, E>> = throw RevisionNotImplementedError
}
