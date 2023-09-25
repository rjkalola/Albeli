package com.ecommerce.albeliapp.dashboard.data.model

class StateInfo(
    var short: String = "",
    var value: String = "",
    var name: String = "",
){

    override fun toString(): String {
        return name
    }

}