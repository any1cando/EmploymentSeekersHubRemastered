package com.example.employmentseekershubremastered

import com.example.employmentseekershubremastered.interfaces.AuthAndRegService
import com.example.employmentseekershubremastered.interfaces.VacancyService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient() {

    private lateinit var authAndRegService: AuthAndRegService
    private lateinit var vacancyService: VacancyService

    /** Метод, чтобы получить настроенный объект, реализующий интерфейс 'AuthAndRegService' */
    fun getAuthAndRegService(): AuthAndRegService {

        // TODO Посмотреть, что за ::

        if (!::authAndRegService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.64:8081/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            authAndRegService = retrofit.create(AuthAndRegService::class.java)
        }

        return authAndRegService
    }

    /** Методы, чтобы получить настроенный объект, реализующий интерфейс 'VacancyService' */
    fun getVacancyService(): VacancyService {
        if (!::vacancyService.isInitialized) {
//            val gson = GsonBuilder()
//                .registerTypeAdapter(FiltersDto::class.java, FilterDataDeserializer())
//                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.64:8081/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            vacancyService = retrofit.create(VacancyService::class.java)
        }

        return vacancyService
    }

}