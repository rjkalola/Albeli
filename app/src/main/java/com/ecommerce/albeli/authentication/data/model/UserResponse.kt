package com.ecommerce.albeli.authentication.data.model

import com.ecommerce.albeli.common.api.model.BaseResponse


data class UserResponse(
    val info: User,
) : BaseResponse()
