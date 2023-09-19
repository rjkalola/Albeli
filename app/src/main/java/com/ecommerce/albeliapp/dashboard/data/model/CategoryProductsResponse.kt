package com.ecommerce.albeliapp.dashboard.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.common.api.model.ModuleInfo


data class CategoryProductsResponse(
    var filter: MutableList<ModuleInfo> = ArrayList(),
    var Data: MutableList<CategoryProductInfo> = ArrayList(),
    val offset: Int = 0,
) : BaseResponse()
