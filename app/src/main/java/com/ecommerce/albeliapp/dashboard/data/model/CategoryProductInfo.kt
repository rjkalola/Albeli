package com.ecommerce.albeliapp.dashboard.data.model

class CategoryProductInfo(
    var id: Int = 0,
    var product_name: String = "",
    var path: String = "",
    var categoryName: String = "",
    var price: ProductPriceInfo= ProductPriceInfo(),
    var selling_price: ProductPriceInfo= ProductPriceInfo(),
    var special_price: ProductPriceInfo = ProductPriceInfo(),
    var wishlisted:Boolean = false,
    var additional_images: MutableList<String> = ArrayList(),
)