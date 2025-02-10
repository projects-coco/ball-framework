package org.coco.domain.model

import org.coco.core.type.BinaryId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.history.Revision
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface RepositoryBase<T : EntityBase> {
    fun findById(id: BinaryId): Optional<T>

    fun findAll(): List<T>

    fun findAll(ids: List<BinaryId>): List<T>

    fun findAll(pageable: Pageable): Page<T>

    fun save(entity: T): T

    fun update(
        id: BinaryId,
        modifier: (T) -> Unit,
    ): T

    fun update(
        entity: T,
        modifier: (T) -> Unit,
    ): T = update(entity.id, modifier)

    fun delete(id: BinaryId)

    fun delete(entity: T) = delete(entity.id)

    fun findRevisions(id: BinaryId): List<Revision<Long, T>>

    fun findRevisions(
        id: BinaryId,
        pageable: Pageable,
    ): Page<Revision<Long, T>>

    fun findRevision(
        id: BinaryId,
        revisionNumber: Long,
    ): Optional<Revision<Long, T>>
}
