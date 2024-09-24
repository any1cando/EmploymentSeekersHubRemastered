package com.example.employmentseekershubremastered.model.dto.main.filters

data class VacancyFilterDto(
    val title: String,
    val filters: List<SealedFilterDto>
)
