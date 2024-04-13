package com.ecommerce.albeli.dashboard.data.model

import com.ecommerce.albeli.common.api.model.BaseResponse


data class AddressResourcesResponse(
    var Data: MutableList<StateInfo> = ArrayList(),
) : BaseResponse()
