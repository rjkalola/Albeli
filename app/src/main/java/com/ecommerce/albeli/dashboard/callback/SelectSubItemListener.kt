package com.ecommerce.albeli.dashboard.callback

interface SelectSubItemListener {
    fun onSelectSubItem(position: Int, parentPosition: Int, action: Int)
}