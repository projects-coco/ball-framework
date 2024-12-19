package org.coco.infra.jpa.model.infra

import org.coco.infra.jpa.helper.JpaRevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface TestEntityJpaRepository : JpaRevisionRepository<TestDataModel>
