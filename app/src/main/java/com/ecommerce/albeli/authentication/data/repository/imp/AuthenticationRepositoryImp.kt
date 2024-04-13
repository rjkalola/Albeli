package com.ecommerce.albeli.authentication.data.repository.imp

import com.ecommerce.albeli.authentication.data.model.UserResponse
import com.ecommerce.albeli.authentication.data.remote.AuthenticationInterface
import com.ecommerce.albeli.authentication.data.repository.AuthenticationRepository
import okhttp3.RequestBody

class AuthenticationRepositoryImp(
    private val authenticationInterface: AuthenticationInterface
) : AuthenticationRepository {
    override suspend fun login(email: RequestBody, password: RequestBody): UserResponse {
        return authenticationInterface.login(email, password)
    }

    override suspend fun register(
        first_name: RequestBody,
        last_name: RequestBody,
        email: RequestBody,
        password: RequestBody
    ): UserResponse {
        return authenticationInterface.register(first_name,last_name, email, password)
    }
}
