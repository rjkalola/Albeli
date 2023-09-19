package com.ecommerce.albeliapp.common.utils

import com.ecommerce.utilities.utils.DateFormatsConstants

object AppConstants {
    const val UNAUTHORIZED = 401
    const val DEVICE_TYPE = 1
    const val CURRENCY = "₤"
    const val MAX_IMAGE_WIDTH = 1280
    const val MAX_IMAGE_HEIGHT = 1280
    const val IMAGE_QUALITY = 80
    const val defaultDateFormat = DateFormatsConstants.DD_MMM_YYYY_SPACE
    const val apiDateFormat = DateFormatsConstants.DD_MM_YYYY_DASH
    const val defaultCountryCode = "GB"
    const val guard = "api"
    const val FCM_ROOM = "chatRooms"
    const val FCM_USERS = "users"
    const val FCM_MESSAGES = "messages"
    const val SERVER_IMAGE_PATH = "https://otmsystem.com/storage/app/user_images/"
    const val EXTRA_CHANNEL_SID = "TileBazar"
    const val DYNAMIC_LINK_PREFIX = "https://owlmanagement.page.link"
    const val SUPPORT_PHONE_NUMBER = "9108093080"

    const val DYNAMIC_LINK_URL = "https://tilebazar.com"

    object IntentKey {
        const val IMAGE_URI = "image_uri"
        const val CROP_RATIO_X = "crop_ratio_X"
        const val CROP_RATIO_Y = "crop_ratio_Y"
        const val FILE_EXTENSION = "file_extension"
        const val NOTIFICATION_TYPE = "NOTIFICATION_TYPE"
        const val IS_FROM_NOTIFICATION = "IS_FROM_NOTIFICATION"
        const val FILTER_MIN_PRICE = "FILTER_MIN_PRICE"
        const val FILTER_MAX_PRICE = "FILTER_MAX_PRICE"
        const val FILTER_DATA = "FILTER_DATA"
        const val FILTER_IDS = "FILTER_IDS"
        const val REQUEST_CROP_IMAGE = 6
        const val WEB_URL = "WEB_URL"
        const val TITLE = "TITLE"


    }

    object SharedPrefKey {
        const val USER_INFO = "USER_INFO"
        const val USERS = "USERS"
        const val APP_URL = "APP_URL"
        const val THEME_MODE = "THEME_MODE"
        const val DEVICE_ID = "DEVICE_ID"
        const val COMPARE_PRODUCT_IDS = "COMPARE_PRODUCT_IDS"
        const val DEFAULT_CATEGORY_ID = "DEFAULT_CATEGORY_ID"
        const val DEFAULT_CATEGORY_NAME = "DEFAULT_CATEGORY_NAME"
    }

    object THEME_MODE {
        const val LIGHT = 0
        const val DARK = 1
    }

    object DialogIdentifier {
        const val LOGOUT = 1
        const val SELECT_CATEGORY = 2
        const val UPDATE_APP = 22

        const val DOB_PICKER = "DOB_PICKER"
    }

    object LocationMode {
        const val LOCATION_MODE_HIGH_ACCURACY = 1
    }

    object Action {
        const val WATCHLIST = 1
        const val PRODUCTS_DETAILS = 2
        const val CATEGORY_DETAILS = 3
    }

    object FileExtension {
        const val JPG = ".jpg"
        const val PNG = ".png"
        const val PDF = ".pdf"
        const val MP3 = ".mp3"
        const val M4A = ".m4a"
    }

    object Type {
        const val CAMERA = "camera"
        const val PDF = "pdf"
        const val SELECT_FROM_CAMERA = 1
        const val SELECT_PHOTOS = 2

        const val RECENTLY_ADDED_PRODUCT = 1
        const val FEATURED_PRODUCT = 2
        const val FIRST_CHOICE_PRODUCT = 3

        const val CALL = 1
        const val WHATSAPP = 2

        const val FREE_USER = 1
        const val PREMIUM_USER = 2

        const val REMOVE_ACCOUNT_OTP = 1
    }

    object Grade {
        const val Premium = 1
        const val Standard = 2
        const val Commercial = 3
        const val Eco = 4
        const val OneTimeLot = 5
    }

    object SortBy {
        const val PRODUCT_NAME_A_TO_Z = 1
        const val PRODUCT_NAME_Z_To_A = 2
        const val PRODUCT_PRICE_LOW_TO_HIGH = 3
        const val PRODUCT_PRICE_HIGH_TO_LOW = 4
        const val PRODUCT_DATE_MODIFIED = 5

        const val SELLER_NAME_A_TO_Z = 1
        const val SELLER_NAME_Z_To_A = 2
        const val SELLER_DATE_MODIFIED = 3
    }

    object DataLimit {
        const val PRODUCTS_LIMIT = 20
        const val DIRECTORY_LIMIT = 10
    }

    object Directory {
        const val DEFAULT = "otmjobs"
        const val IMAGES = "otmjobs/images"
    }

    object Status {
        const val SUCCESS = 1
        const val ERROR = 2
        const val LOADING = 3
    }

}

