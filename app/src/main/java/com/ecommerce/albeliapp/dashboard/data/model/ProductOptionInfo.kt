package com.ecommerce.albeliapp.dashboard.data.model

class ProductOptionInfo(
    var id: Int = 0,
    var name: String = "",
    var opt_value: String = "",
    var values: MutableList<ProductOptionsItemInfo> = ArrayList(),
)