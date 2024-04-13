package com.ecommerce.albeli.common.api

import com.ecommerce.albeli.common.api.model.RegisterConfigurationResponse
import retrofit2.http.*

interface CommonInterface {
    @GET("wn-resources")
    suspend fun companyResource(): RegisterConfigurationResponse
}

