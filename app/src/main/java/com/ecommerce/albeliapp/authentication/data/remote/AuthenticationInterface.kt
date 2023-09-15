package com.ecommerce.albeliapp.authentication.data.remote

import com.ecommerce.albeliapp.authentication.data.model.UserResponse
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthenticationInterface {
    @Multipart
    @POST("login")
    suspend fun login(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): UserResponse

    @Multipart
    @POST("al-kkm")
    suspend fun register(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): UserResponse
}

