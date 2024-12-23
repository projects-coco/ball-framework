package org.coco.infra.mongodb.model

import org.coco.infra.mongodb.helper.CustomMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TestEntityMongoRepository : CustomMongoRepository<TestEntityDocumentModel>
