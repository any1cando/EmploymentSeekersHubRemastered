package com.example.employmentseekershubremastered.model.dto.main.filters

data class VacancyFilterListDto(
    val filters: List<VacancyFilterDto>,
    val pageInfo: PageInfoDto
)