package org.coco.infra.mongodb.model

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TestEntityMongoRepository : MongoRepository<TestEntityDocumentModel, String>
