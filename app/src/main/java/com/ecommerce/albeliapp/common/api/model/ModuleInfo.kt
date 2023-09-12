package com.ecommerce.albeliapp.common.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ModuleInfo(
    var id: Int = 0,
    var position: Int = 0,
    var name: String = "",
    var key: String = "",
    var extension: String = "",
    var flag_name: String = "",
    var extension_with_name: String = "",
    var count: Int = 0,
    var is_selected: Boolean = false,
    var data: MutableList<ModuleInfo> = ArrayList()
) : Parcelable