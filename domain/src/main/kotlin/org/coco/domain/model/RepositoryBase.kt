package org.coco.domain.model

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface RepositoryBase<T : EntityBase> {
    fun findById(id: BinaryId): Optional<T>

    fun findAll(): List<T>

    fun findAll(ids: List<BinaryId>): List<T>

    fun findAll(pageable: Pageable): Page<T>

    fun save(entity: T): T

    fun update(id: BinaryId, modifier: (T) -> Unit)

    fun update(entity: T, modifier: (T) -> Unit) {
        update(entity.id, modifier)
    }

    fun delete(id: BinaryId)
}