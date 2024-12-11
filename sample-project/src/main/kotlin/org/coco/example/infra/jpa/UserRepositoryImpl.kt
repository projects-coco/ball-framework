package org.coco.example.infra.jpa

import org.coco.domain.model.user.BasicUserSearchDto
import org.coco.example.domain.model.user.User
import org.coco.example.domain.model.user.UserRepository
import org.coco.example.infra.jpa.model.user.UserDataModel
import org.coco.example.infra.jpa.model.user.UserJpaRepository
import org.coco.infra.jpa.JpaRepositoryHelper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository
) : UserRepository,
    JpaRepositoryHelper<User, UserDataModel>(jpaRepository, User::class) {
    override fun findByUsername(username: String): Optional<User> =
        jpaRepository.findByUsername(username).map { it.toEntity() }

    override fun save(entity: User): User {
        val dataModel = UserDataModel.of(entity)
        return jpaRepository.save(dataModel).toEntity()
    }

    override fun search(searchDto: BasicUserSearchDto): List<User> {
        TODO("Not yet implemented")
    }

    override fun search(searchDto: BasicUserSearchDto, pageable: Pageable): Page<User> {
        TODO("Not yet implemented")
    }
}