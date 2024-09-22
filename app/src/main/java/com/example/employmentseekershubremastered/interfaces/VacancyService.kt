package com.example.employmentseekershubremastered.interfaces

import com.example.employmentseekershubremastered.model.dto.main.VacancyDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface VacancyService {

    @GET("v1/vacancies")
    fun getVacancies(): Call<List<VacancyDto>>


    @GET("v1/vacancies/{vacancyId}")
    fun getVacancyById(@Path("vacancyId") id: String): Call<VacancyDto>


//    @GET("v1/vacancies/filters")
//    fun getFilters(@Header("Authorization") token: String): Call<List<FiltersDto>>

}