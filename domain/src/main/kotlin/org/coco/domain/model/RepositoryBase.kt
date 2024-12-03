package org.coco.domain.model

import org.coco.domain.core.ErrorType
import org.coco.domain.core.LogicError
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.NoRepositoryBean
import java.util.*
import kotlin.reflect.KClass

class EntityNotFoundError(kClass: KClass<*>, id: BinaryId) :
    LogicError("${kClass.simpleName}(id=$id, hex=${id.toHexString()}) is not found.", ErrorType.NOT_FOUND)

@NoRepositoryBean
interface RepositoryBase<T : EntityBase> {
    fun findById(id: BinaryId): Optional<T>

    fun findAll(): List<T>

    fun findAll(ids: List<BinaryId>): List<T>

    fun findAll(pageable: Pageable): Page<T>

    fun save(entity: T): T

    fun update(id: BinaryId, modifier: (T) -> Unit)

    fun delete(id: BinaryId)
}