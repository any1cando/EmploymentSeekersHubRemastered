package com.example.employmentseekershubremastered.interfaces

import com.example.employmentseekershubremastered.model.dto.entry.point.UserAuthorizationRequest
import com.example.employmentseekershubremastered.model.dto.entry.point.UserRegistrationRequest
import com.example.employmentseekershubremastered.model.dto.entry.point.UserTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthAndRegService {

    // Метод для выполнения запроса на авторизацию на сервер
    @POST("api/v1/auth/login")  // Строка для ввода адреса
    fun performAuthorization(@Body authorizationInfo: UserAuthorizationRequest): Call<UserTokenResponse>

    @POST("api/v1/auth/registration")  // Строка для ввода регистрации
    fun performRegistration(@Body registrationInfo: UserRegistrationRequest): Call<UserTokenResponse>

    @GET("api/v1/greet/hello")
    fun performHello(@Header("Authorization") token: String): Call<String>

}