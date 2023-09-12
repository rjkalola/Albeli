package com.ecommerce.albeliapp.authentication.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse


data class UserResponse(
    val Data: User,
    val IsRegister: Boolean
) : BaseResponse()
