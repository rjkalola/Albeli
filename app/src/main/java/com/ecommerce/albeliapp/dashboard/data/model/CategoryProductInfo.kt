package com.ecommerce.albeliapp.dashboard.data.model

class CategoryProductInfo(
    var id: Int = 0,
    var product_name: String = "",
    var product_description: String = "",
    var product_short_description: String = "",
    var path: String = "",
    var categoryName: String = "",
    var price: ProductPriceInfo= ProductPriceInfo(),
    var selling_price: ProductPriceInfo= ProductPriceInfo(),
    var special_price: ProductPriceInfo = ProductPriceInfo(),
    var sku: String = "",
    var qty: Int = 0,
    var name: String = "",
    var description: String = "",
    var short_description: String = "",
    var wishlisted:Boolean = false,
    var additional_images: MutableList<String> = ArrayList(),
    var total_review: String = "",
    var review_average: String = "",
    var product_options: MutableList<ProductOptionInfo> = ArrayList(),
    var product_attributes: MutableList<ProductAttributeInfo> = ArrayList(),

)