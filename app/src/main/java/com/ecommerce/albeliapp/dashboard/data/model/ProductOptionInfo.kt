package com.ecommerce.albeliapp.dashboard.data.model

class ProductOptionInfo(
    var id: Int = 0,
    var name: String = "",
    var opt_name: String = "",
    var opt_value: String = "",
    var opt_lable: String = "",
    var values: MutableList<ProductOptionsItemInfo> = ArrayList(),
)