package com.ecommerce.albeli.dashboard.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddressInfo(
    var id: Int = 0,
    var user_id: Int = 0,
    var address_1: String = "",
    var address_2: String = "",
    var city: String = "",
    var state: String = "",
    var zip: String = "",
    var country: String = "",
    var first_name: String = "",
    var last_name: String = "",
    var email: String = "",
    var phone: String = "",
    var is_default: String = "",
) : Parcelable