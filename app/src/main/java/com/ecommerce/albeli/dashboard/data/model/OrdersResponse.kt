package com.ecommerce.albeli.dashboard.data.model

import com.ecommerce.albeli.common.api.model.BaseResponse


data class OrdersResponse(
    var Data: MutableList<OrderInfo> = ArrayList(),
    val offset: Int = 0,
) : BaseResponse()
