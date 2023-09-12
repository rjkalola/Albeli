package com.ecommerce.albeliapp.common.api

import com.ecommerce.albeliapp.common.api.model.RegisterConfigurationResponse

class CommonRepositoryImp(
    private val commonInterface: CommonInterface
) : CommonRepository {
    override suspend fun companyResource(): RegisterConfigurationResponse {
        return commonInterface.companyResource()
    }
}
