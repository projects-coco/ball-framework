package org.coco.infra.jpa.model.domain

import org.coco.domain.model.RepositoryBase
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface TestRepository : RepositoryBase<TestEntity>
