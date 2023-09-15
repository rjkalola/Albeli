package com.ecommerce.albeliapp.authentication.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse


data class UserResponse(
    val info: User,
) : BaseResponse()
