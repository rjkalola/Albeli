package com.ecommerce.albeli.dashboard.data.model

import com.ecommerce.albeli.common.api.model.BaseResponse


data class ProductDetailsResponse(
    var Data: CategoryProductInfo = CategoryProductInfo(),
) : BaseResponse()
