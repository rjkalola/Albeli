package com.ecommerce.albeli.common.api

import com.ecommerce.albeli.common.api.model.RegisterConfigurationResponse

class CommonRepositoryImp(
    private val commonInterface: CommonInterface
) : CommonRepository {
    override suspend fun companyResource(): RegisterConfigurationResponse {
        return commonInterface.companyResource()
    }
}
