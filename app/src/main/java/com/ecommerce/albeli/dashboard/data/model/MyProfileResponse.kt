package com.ecommerce.albeli.dashboard.data.model

import com.ecommerce.albeli.common.api.model.BaseResponse


data class MyProfileResponse(
    var first_name: String = "",
    var last_name: String = "",
    var email: String = "",
    var phone: String = "",
) : BaseResponse()
