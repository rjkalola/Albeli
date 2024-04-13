package com.ecommerce.albeli.common.api

import com.ecommerce.albeli.common.api.model.RegisterConfigurationResponse

interface CommonRepository {
    suspend fun companyResource(): RegisterConfigurationResponse
}