package com.ecommerce.albeliapp.authentication.data.model

data class User(
    var id: Int = 0,
    var first_name: String = "",
    var name: String = "",
    var last_name: String = "",
    var email: String = "",
    var api_token: String? = "",
    var image: String? = "",
)