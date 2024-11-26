package org.coco.domain.model

import org.coco.domain.core.Logic
import org.coco.domain.core.logic
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.QueryByExampleExecutor
import java.util.*

@NoRepositoryBean
interface RepositoryBase<T : EntityBase> :
    ListCrudRepository<T, BinaryId>, ListPagingAndSortingRepository<T, BinaryId>, QueryByExampleExecutor<T> {
    sealed interface CrudError {
        data object NotFound : CrudError
    }

    override fun <S : T> findAll(example: Example<S>): MutableIterable<S>

    override fun <S : T> findAll(example: Example<S>, sort: Sort): MutableIterable<S>
}

inline fun <reified T : EntityBase> Optional<T>?.toLogic(): Logic<RepositoryBase.CrudError, T> {
    val optional = this
    return logic {
        if (optional == null || optional.isEmpty) raise(RepositoryBase.CrudError.NotFound)
        optional.get()
    }
}