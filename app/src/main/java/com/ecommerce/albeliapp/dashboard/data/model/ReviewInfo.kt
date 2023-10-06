package com.ecommerce.albeliapp.dashboard.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ReviewInfo(
    var id: Int = 0,
    var reviewer_name: String = "",
    var comment: String = "",
    var rating: String = "",
    var created_at: String = "",
) : Parcelable