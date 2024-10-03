package com.example.employmentseekershubremastered.model.dto.main.filters

data class PageInfoDto(
    val page: Int,
    val size: Int,
    val sorts: List<SortDto>
)
