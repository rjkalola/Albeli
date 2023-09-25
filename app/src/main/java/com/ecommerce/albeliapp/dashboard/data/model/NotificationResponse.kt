package com.ecommerce.albeliapp.dashboard.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.common.api.model.ModuleInfo


data class NotificationResponse(
    var Data: MutableList<NotificationInfo> = ArrayList(),
    var offset: Int = 0,
) : BaseResponse()
