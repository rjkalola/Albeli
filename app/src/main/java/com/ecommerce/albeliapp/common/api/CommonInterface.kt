package com.ecommerce.albeliapp.common.api

import com.ecommerce.albeliapp.common.api.model.RegisterConfigurationResponse
import retrofit2.http.*

interface CommonInterface {
    @GET("wn-resources")
    suspend fun companyResource(): RegisterConfigurationResponse
}

