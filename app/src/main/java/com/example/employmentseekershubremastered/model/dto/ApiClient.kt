package com.example.employmentseekershubremastered.model.dto

import com.example.employmentseekershubremastered.interfaces.AuthAndRegService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient() {
    private lateinit var authService: AuthAndRegService
    // Комментарии убрать, когда перейду к отображению вакансий.
//    private lateinit var vacancyService: VacancyService

    fun getAuthService(): AuthAndRegService {

        if (!::authService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.64:8081/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            authService = retrofit.create(AuthAndRegService::class.java)
        }

        return authService
    }

//    fun getVacancyService(): VacancyService {
//        if (!::vacancyService.isInitialized) {
//            val gson = GsonBuilder()
//                .registerTypeAdapter(FiltersDto::class.java, FilterDataDeserializer())
//                .create()
//
//            val retrofit = Retrofit.Builder()
//                .baseUrl("http://192.168.1.64:8081/api/")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build()
//            vacancyService = retrofit.create(VacancyService::class.java)
//        }
//
//        return vacancyService
//    }

}