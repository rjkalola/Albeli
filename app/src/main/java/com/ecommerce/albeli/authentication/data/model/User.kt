package com.ecommerce.albeli.authentication.data.model

data class User(
    var id: Int = 0,
    var first_name: String = "",
    var name: String = "",
    var last_name: String = "",
    var email: String = "",
    var phone: String = "",
    var api_token: String? = "",
    var image: String? = "",
)