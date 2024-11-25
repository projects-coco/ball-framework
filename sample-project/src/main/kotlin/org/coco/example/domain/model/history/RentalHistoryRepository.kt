package org.coco.example.domain.model.history

import org.coco.domain.model.RepositoryBase
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface RentalHistoryRepository : RepositoryBase<RentalHistory>
