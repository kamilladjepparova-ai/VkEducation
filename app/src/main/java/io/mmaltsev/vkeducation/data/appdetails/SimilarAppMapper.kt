package io.mmaltsev.vkeducation.data.appdetails

import io.mmaltsev.vkeducation.domain.appdetails.SimilarApp

class SimilarAppMapper {
    fun toDomain(dto: SimilarAppDto): SimilarApp = SimilarApp(
        id = dto.id,
        name = dto.name,
        iconUrl = dto.iconUrl,
    )

    fun toDto(domain: SimilarApp): SimilarAppDto = SimilarAppDto(
        id = domain.id,
        name = domain.name,
        iconUrl = domain.iconUrl,
    )
}