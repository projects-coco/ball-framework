package org.coco.example.infra.jpa

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.dsl.jpql.select.SelectQueryWhereStep
import com.linecorp.kotlinjdsl.querymodel.jpql.path.Path
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicatable
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import jakarta.persistence.EntityManager
import org.coco.core.extension.bindOrNull
import org.coco.core.type.BinaryId
import org.coco.domain.model.user.BasicUser.*
import org.coco.domain.model.user.BasicUserSearchDto
import org.coco.example.domain.model.user.User
import org.coco.example.domain.model.user.UserRepository
import org.coco.example.infra.jpa.model.user.UserDataModel
import org.coco.example.infra.jpa.model.user.UserJpaRepository
import org.coco.infra.jpa.core.selectCount
import org.coco.infra.jpa.core.selectFrom
import org.coco.infra.jpa.helper.JpaSearchRepositoryHelper
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.util.*

@Repository
@Transactional
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
    jpqlRenderContext: JpqlRenderContext,
    entityManager: EntityManager,
) : JpaSearchRepositoryHelper<User, UserDataModel, BasicUserSearchDto>(
        jpaRepository,
        User::class,
        entityManager,
        jpqlRenderContext,
    ),
    UserRepository {
    override fun UserDataModel.toEntity(): User =
        User(
            id = BinaryId(id),
            username = Username(username),
            roles = roles.map { User.Role.valueOf(it) }.toSet(),
            name = Name(name),
            phoneNumber = PhoneNumber(phoneNumber),
            passwordHash = PasswordHash(passwordHash),
            agreementOfTerms = agreementOfTerms,
            agreementOfPrivacy = agreementOfPrivacy,
            active = active,
            lastLoginAt = lastLoginAt,
            loginCount = loginCount,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    override fun User.toModel(): UserDataModel = UserDataModel.of(this)

    override fun findByUsername(username: String): Optional<User> = jpaRepository.findByUsername(username).map { it.toEntity() }

    override fun Jpql.selectFrom(): SelectQueryWhereStep<UserDataModel> = selectFrom(UserDataModel::class)

    override fun Jpql.selectCount(): SelectQueryWhereStep<Long> = selectCount(UserDataModel::class)

    override fun Jpql.where(searchDto: BasicUserSearchDto): Array<out Predicatable?> =
        searchDto.run {
            arrayOf(
                id.bindOrNull { path(UserDataModel::id).eq(it.value) },
                username.bindOrNull { path(UserDataModel::username).like("%$it%") },
                name.bindOrNull { path(UserDataModel::name).like("%$it%") },
                phoneNumber.bindOrNull { path(UserDataModel::phoneNumber).like("%$it%") },
                active.bindOrNull { path(UserDataModel::active).eq(it) },
                loginCount.bindOrNull { path(UserDataModel::loginCount).eq(it) },
            )
        }

    override fun Jpql.orderBy(order: Sort.Order): Path<out Serializable> =
        when (order.property) {
            "username" -> path(UserDataModel::username)
            "name" -> path(UserDataModel::name)
            "phoneNumber" -> path(UserDataModel::phoneNumber)
            "active" -> path(UserDataModel::active)
            "loginCount" -> path(UserDataModel::loginCount)
            else -> path(UserDataModel::id)
        }
}
