package org.coco.infra.mongodb.helper

import org.bson.types.ObjectId
import org.coco.infra.mongodb.model.DocumentModel
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface CustomMongoRepository<T : DocumentModel<*>> : MongoRepository<T, ObjectId> {
    fun findByEntityId(entityId: String): Optional<T>

    fun findAllByEntityId(entityIds: List<String>): List<T>
}
