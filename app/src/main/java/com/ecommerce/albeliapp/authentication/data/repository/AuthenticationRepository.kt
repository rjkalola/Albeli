package com.ecommerce.albeliapp.authentication.data.repository

import com.ecommerce.albeliapp.authentication.data.model.UserResponse
import okhttp3.RequestBody

interface AuthenticationRepository {

    suspend fun login(
        email: RequestBody,
        password: RequestBody
    ): UserResponse

    suspend fun register(
        first_name: RequestBody,
        last_name: RequestBody,
        email: RequestBody,
        password: RequestBody
    ): UserResponse
}