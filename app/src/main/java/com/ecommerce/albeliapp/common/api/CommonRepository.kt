package com.ecommerce.albeliapp.common.api

import com.ecommerce.albeliapp.common.api.model.RegisterConfigurationResponse

interface CommonRepository {
    suspend fun companyResource(): RegisterConfigurationResponse
}