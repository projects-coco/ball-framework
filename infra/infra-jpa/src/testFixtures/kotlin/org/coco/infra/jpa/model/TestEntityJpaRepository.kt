package org.coco.infra.jpa.model

import org.coco.infra.jpa.helper.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface TestEntityJpaRepository : JpaRevisionRepository<TestEntityDataModel>
