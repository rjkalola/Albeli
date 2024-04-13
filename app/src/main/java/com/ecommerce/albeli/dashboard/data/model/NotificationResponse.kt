package com.ecommerce.albeli.dashboard.data.model

import com.ecommerce.albeli.common.api.model.BaseResponse


data class NotificationResponse(
    var Data: MutableList<NotificationInfo> = ArrayList(),
    var offset: Int = 0,
) : BaseResponse()
