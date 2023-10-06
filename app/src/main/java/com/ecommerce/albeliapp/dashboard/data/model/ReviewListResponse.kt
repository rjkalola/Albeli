package com.ecommerce.albeliapp.dashboard.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.common.api.model.ModuleInfo


data class ReviewListResponse(
    var Data: MutableList<ReviewInfo> = ArrayList(),
    var offset:Int = 0
) : BaseResponse()
