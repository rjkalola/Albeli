package com.ecommerce.albeli.dashboard.data.model

import com.ecommerce.albeli.common.api.model.BaseResponse


data class ReviewListResponse(
    var Data: MutableList<ReviewInfo> = ArrayList(),
    var offset:Int = 0
) : BaseResponse()
