package com.ecommerce.albeli.common.callback

interface SelectItemListener {
    fun onSelectItem(position: Int, action: Int,productType: Int)
}