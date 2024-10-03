package com.example.employmentseekershubremastered.interfaces

import com.example.employmentseekershubremastered.model.dto.main.VacancyDto
import com.example.employmentseekershubremastered.model.dto.main.filters.VacancyFilterListDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VacancyService {

    @GET("api/v1/vacancies")
    fun getVacancies(): Call<List<VacancyDto>>


    @GET("api/v1/vacancies/{vacancyId}")
    fun getVacancyById(@Path("vacancyId") id: String): Call<VacancyDto>


    @GET("api/v1/vacancies/filters")
    fun getFilters(): Call<VacancyFilterListDto>


    @POST("api/v1/vacancies/filters")
    fun sendFilters(@Body sendingFilters: VacancyFilterListDto): Call<List<VacancyDto>>

}