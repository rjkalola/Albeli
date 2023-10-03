package com.ecommerce.albeliapp.dashboard.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.common.api.model.ModuleInfo


data class OrdersResponse(
    var Data: MutableList<OrderInfo> = ArrayList(),
    val offset: Int = 0,
) : BaseResponse()
