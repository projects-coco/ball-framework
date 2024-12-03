package org.coco.domain.model

import org.coco.domain.core.ErrorType
import org.coco.domain.core.LogicError
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.QueryByExampleExecutor
import kotlin.reflect.KClass

class EntityNotFoundError(kClass: KClass<*>, id: BinaryId) :
    LogicError("${kClass.simpleName}(id=$id, hex=${id.toHexString()}) is not found.", ErrorType.NOT_FOUND)

@NoRepositoryBean
interface RepositoryBase<T : EntityBase> :
    ListCrudRepository<T, BinaryId>, ListPagingAndSortingRepository<T, BinaryId>, QueryByExampleExecutor<T> {
    override fun <S : T> findAll(example: Example<S>): MutableIterable<S>

    override fun <S : T> findAll(example: Example<S>, sort: Sort): MutableIterable<S>

    fun update(id: BinaryId, modifier: (T) -> Unit)
}