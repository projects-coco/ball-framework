package org.coco.infra.jpa.helper

import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.coco.domain.model.EntityNotFoundError
import org.coco.domain.model.RepositoryBase
import org.coco.infra.jpa.model.DataModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.reflect.KClass

@Transactional
abstract class JpaRepositoryHelper<E : EntityBase, D : DataModel<E>>(
    private val jpaRepository: JpaRepository<D, ByteArray>,
    private val entityClass: KClass<E>
) : RepositoryBase<E> {
    override fun findById(id: BinaryId): Optional<E> = jpaRepository.findById(id.value).map { it.toEntity() }

    override fun findAll(): List<E> = jpaRepository.findAll().map { it.toEntity() }

    override fun findAll(ids: List<BinaryId>): List<E> =
        jpaRepository.findAllById(ids.map { it.value }).map { it.toEntity() }

    override fun findAll(pageable: Pageable): Page<E> = jpaRepository.findAll(pageable).map { it.toEntity() }

    override fun update(id: BinaryId, modifier: (E) -> Unit) {
        val dataModel = jpaRepository.findById(id.value).orElseThrow { EntityNotFoundError(entityClass, id) }
        val entity = dataModel.toEntity()
        modifier.invoke(entity)
        dataModel.update(entity)
    }

    override fun delete(id: BinaryId) {
        jpaRepository.deleteById(id.value)
    }
}