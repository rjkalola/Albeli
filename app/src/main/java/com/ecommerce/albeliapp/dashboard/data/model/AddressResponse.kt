package com.ecommerce.albeliapp.dashboard.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse


data class AddressResponse(
    var Data: MutableList<AddressInfo> = ArrayList(),
) : BaseResponse()