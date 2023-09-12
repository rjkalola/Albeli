package com.ecommerce.albeliapp.common.callback

import android.location.Location

interface LocationUpdateCallBack {
    fun locationUpdate(location: Location)
}