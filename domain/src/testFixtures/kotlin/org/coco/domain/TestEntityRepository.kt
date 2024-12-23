package org.coco.domain

import org.coco.domain.model.RepositoryBase
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface TestEntityRepository : RepositoryBase<TestEntity>
