package org.coco.infra.jpa.helper

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.dsl.jpql.select.SelectQueryWhereStep
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicatable
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import org.coco.core.type.BinaryId
import org.coco.domain.model.EntityBase
import org.coco.domain.model.SearchDto
import org.coco.domain.model.SearchRepository
import org.coco.infra.jpa.model.DataModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import kotlin.reflect.KClass

abstract class JpaSearchRepositoryHelper<E : EntityBase, D : DataModel<E>, S : SearchDto>(
    jpaRepository: JpaRepository<D, ByteArray>,
    entityClass: KClass<E>,
    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : JpaRepositoryHelper<E, D>(jpaRepository, entityClass),
    SearchRepository<E, S> {
    override fun search(searchDto: S): List<E> {
        val query =
            jpql {
                selectFrom()
                    .whereAnd(*where(searchDto))
            }
        val jpaQuery = entityManager.createQuery(query, jpqlRenderContext)
        return jpaQuery.resultList.map { it.toEntity() }
    }

    override fun search(
        searchDto: S,
        pageable: Pageable,
    ): Page<E> {
        val contentQuery =
            jpql {
                selectFrom()
                    .whereAnd(*where(searchDto))
            }
        val countQuery =
            jpql {
                selectCount()
                    .whereAnd(*where(searchDto))
            }
        val content =
            entityManager
                .createQuery(contentQuery, jpqlRenderContext)
                .apply {
                    firstResult = pageable.offset.toInt()
                    setMaxResults(pageable.pageSize)
                }.resultList
                .map { it.toEntity() }
        val count = entityManager.createQuery(countQuery, jpqlRenderContext).singleResult as Long
        return PageImpl(content, pageable, count)
    }

    fun searchAsId(searchDto: S): List<BinaryId> {
        val query =
            jpql {
                selectFrom()
                    .whereAnd(*where(searchDto))
            }
        val jpaQuery = entityManager.createQuery(query, jpqlRenderContext)
        return jpaQuery.resultList.map { BinaryId(it.id) }
    }

    abstract fun Jpql.selectFrom(): SelectQueryWhereStep<D>

    abstract fun Jpql.selectCount(): SelectQueryWhereStep<Long>

    abstract fun Jpql.where(searchDto: S): Array<out Predicatable?>
}
