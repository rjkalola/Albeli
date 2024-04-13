package com.ecommerce.albeli.dashboard.data.model

import com.ecommerce.albeli.common.api.model.BaseResponse


data class AddressResponse(
    var Data: MutableList<AddressInfo> = ArrayList(),
) : BaseResponse()