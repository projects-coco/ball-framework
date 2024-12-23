package org.coco.example.domain.model.memo

import org.coco.core.type.BinaryId
import org.coco.domain.model.RepositoryBase
import java.util.*

interface MemoRepository : RepositoryBase<Memo> {
    fun findByTargetId(targetId: BinaryId): Optional<Memo>
}
