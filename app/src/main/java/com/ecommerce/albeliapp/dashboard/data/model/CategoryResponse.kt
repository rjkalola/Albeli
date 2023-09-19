package com.ecommerce.albeliapp.dashboard.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.common.api.model.ModuleInfo


data class CategoryResponse(
    var Data: MutableList<ModuleInfo> = ArrayList(),
) : BaseResponse()
