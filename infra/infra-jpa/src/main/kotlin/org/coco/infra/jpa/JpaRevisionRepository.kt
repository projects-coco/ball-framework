package org.coco.infra.jpa

import org.coco.domain.model.BinaryId
import org.coco.infra.jpa.model.DataModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.history.RevisionRepository

@NoRepositoryBean
interface JpaRevisionRepository<T : DataModel<*>> :
    JpaRepository<T, BinaryId>,
    RevisionRepository<T, BinaryId, Long>