package com.ecommerce.albeliapp.common.callback

interface SelectItemListener {
    fun onSelectItem(position: Int, action: Int,productType: Int)
}