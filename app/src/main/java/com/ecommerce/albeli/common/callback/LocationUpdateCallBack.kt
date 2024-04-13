package com.ecommerce.albeli.common.callback

import android.location.Location

interface LocationUpdateCallBack {
    fun locationUpdate(location: Location)
}