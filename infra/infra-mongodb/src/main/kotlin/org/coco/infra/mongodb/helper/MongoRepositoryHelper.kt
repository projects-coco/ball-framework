package org.coco.infra.mongodb.helper

import org.coco.core.type.BinaryId
import org.coco.domain.exception.EntityNotFoundError
import org.coco.domain.model.EntityBase
import org.coco.domain.model.RepositoryBase
import org.coco.infra.mongodb.model.DocumentModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.reflect.KClass

@Transactional
abstract class MongoRepositoryHelper<E : EntityBase, D : DocumentModel<E>>(
    private val mongoRepository: MongoRepository<D, ByteArray>,
    private val entityClass: KClass<E>,
    private val documentModelClass: KClass<D>,
    private val mongoTemplate: MongoTemplate,
) : RepositoryBase<E> {
    override fun findById(id: BinaryId): Optional<E> = mongoRepository.findById(id.value).map { it.toEntity() }

    override fun findAll(): List<E> = mongoRepository.findAll().map { it.toEntity() }

    override fun findAll(ids: List<BinaryId>): List<E> = mongoRepository.findAllById(ids.map { it.value }).map { it.toEntity() }

    override fun findAll(pageable: Pageable): Page<E> = mongoRepository.findAll(pageable).map { it.toEntity() }

    override fun update(
        id: BinaryId,
        modifier: (E) -> Unit,
    ) {
        val documentModel = mongoRepository.findById(id.value).orElseThrow { EntityNotFoundError(entityClass, id) }
        val entity = documentModel.toEntity()
        modifier.invoke(entity)
        mongoTemplate.updateFirst(
            Query(Criteria.where("entityId").`is`(id.value)),
            documentModel.update(entity),
            documentModelClass.java,
        )
    }

    override fun delete(id: BinaryId) {
        mongoRepository.deleteById(id.value)
    }
}
