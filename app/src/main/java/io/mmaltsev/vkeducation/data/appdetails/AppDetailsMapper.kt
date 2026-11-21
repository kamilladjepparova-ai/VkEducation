package io.mmaltsev.vkeducation.data.appdetails

import io.mmaltsev.vkeducation.domain.appdetails.AppDetails
import io.mmaltsev.vkeducation.domain.appdetails.Category

class AppDetailsMapper {
    fun toDomain(dto: AppDetailsDto): AppDetails = AppDetails(
        id = dto.id,
        name = dto.name,
        developer = dto.developer,
        category = dto.category, // Теперь Category найдётся благодаря импорту выше
        ageRating = dto.ageRating,
        size = dto.size.toFloat(),
        iconUrl = dto.icon,
        screenshotUrlList = dto.screenshots,
        description = dto.description,
    )

    fun toDto(domain: AppDetails): AppDetailsDto = AppDetailsDto(
        id = domain.id,
        name = domain.name,
        developer = domain.developer,
        category = domain.category,
        ageRating = domain.ageRating,
        size = domain.size.toDouble(),
        icon = domain.iconUrl,
        screenshots = domain.screenshotUrlList,
        description = domain.description,
    )
}