package com.ecommerce.albeliapp.dashboard.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse


data class ProductDetailsResponse(
    var Data: CategoryProductInfo = CategoryProductInfo(),
) : BaseResponse()
