package org.coco.infra.jpa

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.dsl.jpql.select.SelectQueryWhereStep
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicatable
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.coco.domain.model.EntityBase
import org.coco.domain.model.SearchDto
import org.coco.domain.model.SearchRepository
import org.coco.infra.jpa.model.DataModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import kotlin.reflect.KClass

abstract class JpaSearchRepositoryHelper<E : EntityBase, D : DataModel<E>, S : SearchDto>(
    private val jpaRepository: JpaRepository<D, ByteArray>,
    private val entityClass: KClass<E>,
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : JpaRepositoryHelper<E, D>(jpaRepository, entityClass), SearchRepository<E, S> {
    override fun search(searchDto: S): List<E> {
        return kotlinJdslJpqlExecutor.findAll {
            selectFrom()
                .whereAnd(*where(searchDto))
        }.map { it!!.toEntity() }
    }

    override fun search(searchDto: S, pageable: Pageable): Page<E> {
        return kotlinJdslJpqlExecutor.findPage(pageable) {
            selectFrom()
                .whereAnd(*where(searchDto))
        }.map { it!!.toEntity() }
    }

    abstract fun Jpql.selectFrom(): SelectQueryWhereStep<D>

    abstract fun Jpql.where(searchDto: S): Array<out Predicatable?>
}