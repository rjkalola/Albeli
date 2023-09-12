package com.ecommerce.albeliapp.common.callback

import com.ecommerce.albeliapp.common.api.model.PostCodeInfo


interface SelectPostCodeListener {
    fun onSelectPostCode(info: PostCodeInfo, tag: Int)
}