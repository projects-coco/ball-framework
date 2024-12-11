package org.coco.infra.jpa.helper

import org.coco.core.type.BinaryId
import org.coco.core.type.LogicError
import org.coco.domain.exception.EntityNotFoundError
import org.coco.domain.model.EntityBase
import org.coco.domain.model.RepositoryBase
import org.coco.infra.jpa.model.DataModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.history.Revision
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.reflect.KClass

@Transactional
abstract class JpaRepositoryHelper<E : EntityBase, D : DataModel<E>>(
    private val jpaRepository: JpaRepository<D, ByteArray>,
    private val entityClass: KClass<E>,
) : RepositoryBase<E> {
    companion object {
        val RevisionNotImplementedError = LogicError("Revision 을 찾을 수 없습니다.")
    }

    override fun findById(id: BinaryId): Optional<E> = jpaRepository.findById(id.value).map { it.toEntity() }

    override fun findAll(): List<E> = jpaRepository.findAll().map { it.toEntity() }

    override fun findAll(ids: List<BinaryId>): List<E> = jpaRepository.findAllById(ids.map { it.value }).map { it.toEntity() }

    override fun findAll(pageable: Pageable): Page<E> = jpaRepository.findAll(pageable).map { it.toEntity() }

    override fun update(
        id: BinaryId,
        modifier: (E) -> Unit,
    ) {
        val dataModel = jpaRepository.findById(id.value).orElseThrow { EntityNotFoundError(entityClass, id) }
        val entity = dataModel.toEntity()
        modifier.invoke(entity)
        dataModel.update(entity)
    }

    override fun delete(id: BinaryId) {
        jpaRepository.deleteById(id.value)
    }

    override fun findRevisions(id: BinaryId): List<Revision<Long, E>> {
        if (jpaRepository is JpaRevisionRepository<D>) {
            val revisions = jpaRepository.findRevisions(id.value)
            return revisions
                .map {
                    Revision.of(it.metadata, it.entity.toEntity())
                }
                .toList()
        } else {
            throw RevisionNotImplementedError
        }
    }

    override fun findRevisions(
        id: BinaryId,
        pageable: Pageable,
    ): Page<Revision<Long, E>> {
        if (jpaRepository is JpaRevisionRepository<D>) {
            val revisions = jpaRepository.findRevisions(id.value, pageable)
            return revisions.map {
                Revision.of(it.metadata, it.entity.toEntity())
            }
        } else {
            throw RevisionNotImplementedError
        }
    }

    override fun findRevision(
        id: BinaryId,
        revisionNumber: Long,
    ): Optional<Revision<Long, E>> {
        if (jpaRepository is JpaRevisionRepository<D>) {
            val revision = jpaRepository.findRevision(id.value, revisionNumber)
            return revision.map {
                Revision.of(it.metadata, it.entity.toEntity())
            }
        } else {
            throw RevisionNotImplementedError
        }
    }
}
