package org.coco.infra.jpa.core

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.dsl.jpql.select.SelectQueryWhereStep
import org.coco.infra.jpa.model.DataModel
import kotlin.reflect.KClass

inline fun <reified T : DataModel<*>> Jpql.selectFrom(dataModelKClass: KClass<T>): SelectQueryWhereStep<T> =
    select(entity(dataModelKClass))
        .from(entity(dataModelKClass))

inline fun <reified T : DataModel<*>> Jpql.selectCount(dataModelKClass: KClass<T>): SelectQueryWhereStep<Long> =
    select(count(entity(dataModelKClass)))
        .from(entity(dataModelKClass))
