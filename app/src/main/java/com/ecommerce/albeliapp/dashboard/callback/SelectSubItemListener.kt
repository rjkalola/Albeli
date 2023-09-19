package com.ecommerce.albeliapp.dashboard.callback

interface SelectSubItemListener {
    fun onSelectSubItem(position: Int, parentPosition: Int, action: Int)
}