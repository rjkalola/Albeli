package com.ecommerce.albeliapp.common.utils

import com.ecommerce.albeliapp.BuildConfig

object VariantConfig {
    val serverBaseUrl: String
        get() {
            return BuildConfig.SERVER_URL
        }
}