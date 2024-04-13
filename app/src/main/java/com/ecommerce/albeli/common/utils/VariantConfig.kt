package com.ecommerce.albeli.common.utils

import com.ecommerce.albeli.BuildConfig

object VariantConfig {
    val serverBaseUrl: String
        get() {
            return BuildConfig.SERVER_URL
        }
}