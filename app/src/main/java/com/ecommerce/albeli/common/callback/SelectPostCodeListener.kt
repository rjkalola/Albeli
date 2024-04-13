package com.ecommerce.albeli.common.callback

import com.ecommerce.albeli.common.api.model.PostCodeInfo


interface SelectPostCodeListener {
    fun onSelectPostCode(info: PostCodeInfo, tag: Int)
}