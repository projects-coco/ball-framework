package org.coco.infra.redis.helper

import org.coco.core.type.BinaryId
import org.coco.core.type.LogicError
import org.coco.domain.exception.EntityNotFoundError
import org.coco.domain.model.EntityBase
import org.coco.domain.model.RepositoryBase
import org.coco.infra.redis.model.HashModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.history.Revision
import org.springframework.data.repository.CrudRepository
import java.util.*
import kotlin.reflect.KClass

abstract class RedisRepositoryHelper<E : EntityBase, H : HashModel<E>>(
    private val redisRepository: CrudRepository<H, String>,
    private val entityClass: KClass<E>,
) : RepositoryBase<E> {
    companion object {
        val PaginationNotSupportedError = LogicError("Paging 을 지원하지 않습니다.")
        val RevisionNotSupportedError = LogicError("Revision 을 찾을 수 없습니다.")
    }

    override fun findAll(pageable: Pageable): Page<E> = throw PaginationNotSupportedError

    override fun findAll(ids: List<BinaryId>): List<E> = redisRepository.findAllById(ids.map { it.toString() }).map { it.toEntity() }

    override fun findAll(): List<E> = redisRepository.findAll().map { it.toEntity() }

    override fun findById(id: BinaryId): Optional<E> = redisRepository.findById(id.toString()).map { it.toEntity() }

    override fun update(
        id: BinaryId,
        modifier: (E) -> Unit,
    ) {
        val hashModel = redisRepository.findById(id.toString()).orElseThrow { EntityNotFoundError(entityClass, id) }
        val entity = hashModel.toEntity()
        modifier.invoke(entity)
        hashModel.update(entity)
        redisRepository.save(hashModel)
    }

    override fun delete(id: BinaryId) = redisRepository.deleteById(id.toString())

    override fun findRevision(
        id: BinaryId,
        revisionNumber: Long,
    ): Optional<Revision<Long, E>> = throw RevisionNotSupportedError

    override fun findRevisions(
        id: BinaryId,
        pageable: Pageable,
    ): Page<Revision<Long, E>> = throw RevisionNotSupportedError

    override fun findRevisions(id: BinaryId): List<Revision<Long, E>> = throw RevisionNotSupportedError
}
