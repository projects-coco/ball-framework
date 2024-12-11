package org.coco.domain.model

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface SearchDto

interface SearchRepository<T : EntityBase, S : SearchDto> {
    fun search(searchDto: S): List<T>

    fun search(
        searchDto: S,
        pageable: Pageable,
    ): Page<T>

    fun searchOne(searchDto: S): Optional<T> {
        return Optional.ofNullable(search(searchDto).firstOrNull())
    }
}
