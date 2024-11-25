package org.coco.infra.jpa

import org.coco.domain.model.BinaryId
import org.coco.domain.model.EntityBase
import org.springframework.data.repository.history.RevisionRepository

interface JpaRevisionRepository<T : EntityBase> : RevisionRepository<T, BinaryId, Long>