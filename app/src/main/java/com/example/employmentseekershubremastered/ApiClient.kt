package com.example.employmentseekershubremastered

import com.example.employmentseekershubremastered.interfaces.AuthAndRegService
import com.example.employmentseekershubremastered.interfaces.VacancyService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient() {

    private lateinit var authAndRegService: AuthAndRegService
    private lateinit var vacancyService: VacancyService
    private val client = getClient()

    // Настройка OkHttpClient3
    private fun getClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return client
    }

    /** Метод, чтобы получить настроенный объект, реализующий интерфейс 'AuthAndRegService' */
    fun getAuthAndRegService(): AuthAndRegService {

        // TODO Посмотреть, что за ::

        if (!::authAndRegService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.64:8081/api/")
                .client(client)
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
                .baseUrl("http://192.168.1.79:8081/api/")  // Либо 192.168.1.64, если GPON5 сеть
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            vacancyService = retrofit.create(VacancyService::class.java)
        }

        return vacancyService
    }

}