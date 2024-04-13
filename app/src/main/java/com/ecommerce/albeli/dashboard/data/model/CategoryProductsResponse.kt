package com.ecommerce.albeli.dashboard.data.model

import com.ecommerce.albeli.common.api.model.BaseResponse
import com.ecommerce.albeli.common.api.model.ModuleInfo


data class CategoryProductsResponse(
    var filter: MutableList<ModuleInfo> = ArrayList(),
    var Data: MutableList<CategoryProductInfo> = ArrayList(),
    val offset: Int = 0,
) : BaseResponse()
