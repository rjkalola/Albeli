package com.ecommerce.albeli.dashboard.data.model

import com.ecommerce.albeli.common.api.model.BaseResponse
import com.ecommerce.albeli.common.api.model.ModuleInfo


data class CategoryResponse(
    var Data: MutableList<ModuleInfo> = ArrayList(),
) : BaseResponse()
