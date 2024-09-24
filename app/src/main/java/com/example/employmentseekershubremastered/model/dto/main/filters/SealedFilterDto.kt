package com.example.employmentseekershubremastered.model.dto.main.filters

sealed class SealedFilterDto {
    data class SearchFilterDto(
        val type: String,
        val data: String
    ) : SealedFilterDto()

    data class RangeFilterDto(
        val type: String,
        val data: RangeDto
    ) : SealedFilterDto()

    data class CheckBoxFilterDto(
        val type: String,
        val data: List<CheckBoxDto>
    ) : SealedFilterDto()
}