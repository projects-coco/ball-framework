package org.coco.example.infra.jpa

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.dsl.jpql.select.SelectQueryWhereStep
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicatable
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import jakarta.persistence.EntityManager
import org.coco.domain.core.bindOrNull
import org.coco.domain.model.user.BasicUserSearchDto
import org.coco.example.domain.model.user.User
import org.coco.example.domain.model.user.UserRepository
import org.coco.example.infra.jpa.model.user.UserDataModel
import org.coco.example.infra.jpa.model.user.UserJpaRepository
import org.coco.infra.jpa.JpaSearchRepositoryHelper
import org.coco.infra.jpa.core.selectCount
import org.coco.infra.jpa.core.selectFrom
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
    jpqlRenderContext: JpqlRenderContext,
    entityManager: EntityManager,
) : UserRepository,
    JpaSearchRepositoryHelper<User, UserDataModel, BasicUserSearchDto>(
        jpaRepository,
        User::class,
        entityManager,
        jpqlRenderContext
    ) {
    override fun findByUsername(username: String): Optional<User> =
        jpaRepository.findByUsername(username).map { it.toEntity() }

    override fun save(entity: User): User {
        val dataModel = UserDataModel.of(entity)
        return jpaRepository.save(dataModel).toEntity()
    }

    override fun Jpql.selectFrom(): SelectQueryWhereStep<UserDataModel> = selectFrom(UserDataModel::class)

    override fun Jpql.selectCount(): SelectQueryWhereStep<Long> = selectCount(UserDataModel::class)

    override fun Jpql.where(searchDto: BasicUserSearchDto): Array<out Predicatable?> = searchDto.run {
        arrayOf(
            id.bindOrNull { path(UserDataModel::id).eq(it.value) },
            username.bindOrNull { path(UserDataModel::username).like("%$it%") },
            name.bindOrNull { path(UserDataModel::name).like("%$it%") },
            phoneNumber.bindOrNull { path(UserDataModel::phoneNumber).like("%$it%") },
            active.bindOrNull { path(UserDataModel::active).eq(it) },
            loginCount.bindOrNull { path(UserDataModel::loginCount).eq(it) },
        )
    }
}