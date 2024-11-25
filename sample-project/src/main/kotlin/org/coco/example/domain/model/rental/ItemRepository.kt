package org.coco.example.domain.model.rental

import org.coco.domain.model.RepositoryBase
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface ItemRepository : RepositoryBase<Item>
