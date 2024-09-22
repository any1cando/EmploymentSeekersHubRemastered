package com.example.employmentseekershubremastered.model.dto.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VacancyDto(
    val id: String,
    val vacancyTitle: String, // Название вакансии
    val companyId: String,  // ID компании
    val companyTitle: String, // Название компании
    val countCandidates: Int,  // Количество откликов ['applicants']
    val tags: List<String?>, // Теги вакансии
    val description: String,  // Описание вакансии (будет ограничено по размеру)
    val salary: SalaryDto,  // Зарплата
    val postedTime: String,  // Дата публикации вакансии
) : Parcelable

