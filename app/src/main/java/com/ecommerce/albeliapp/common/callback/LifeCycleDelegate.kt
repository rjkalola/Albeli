package com.ecommerce.albeliapp.common.callback

interface LifeCycleDelegate {
    fun onAppBackgrounded()
    fun onAppForegrounded()
}