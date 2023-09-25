package com.ecommerce.albeliapp.dashboard.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.common.api.model.ModuleInfo


data class AddressResourcesResponse(
    var Data: MutableList<StateInfo> = ArrayList(),
) : BaseResponse()
