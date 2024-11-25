package org.coco.domain.model

import arrow.core.Either
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

    fun <T> Optional<T>?.toEither(): Either<CrudError, T> {
        if (this == null || this.isEmpty) return Either.Left(CrudError.NotFound)
        return Either.Right(
            this.get(),
        )
    }

    override fun <S : T> findAll(example: Example<S>): MutableIterable<S>

    override fun <S : T> findAll(example: Example<S>, sort: Sort): MutableIterable<S>
}
