package com.ecommerce.albeliapp.common.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ModuleInfo(
    var id: Int = 0,
    var position: Int = 0,
    var name: String = "",
    var value: String = "",
    var values: MutableList<ModuleInfo> = ArrayList(),
    var check: Boolean = false
) : Parcelable