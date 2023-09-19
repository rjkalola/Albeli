package com.ecommerce.albeliapp.dashboard.data.model

import com.ecommerce.albeliapp.common.api.model.BaseResponse


data class DashboardResponse(
    val categoryID: Int = 0,
    val categoryName: String= "",
    var mainSlider: List<String> = ArrayList(),
    var categorySlider: MutableList<DashboardCategoryInfo> = ArrayList(),
    var footerSlider: MutableList<DashboardCategoryInfo> = ArrayList()
) : BaseResponse()
