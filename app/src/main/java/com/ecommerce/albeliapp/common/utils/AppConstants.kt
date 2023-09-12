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
        const val PHONE_NUMBER = "PHONE_NUMBER"
        const val SIGN_UP_REQUEST_DATA = "SIGN_UP_REQUEST_DATA"
        const val IMAGE_URI = "image_uri"
        const val CROP_RATIO_X = "crop_ratio_X"
        const val CROP_RATIO_Y = "crop_ratio_Y"
        const val FILE_EXTENSION = "file_extension"
        const val PRODUCT_INFO = "PRODUCT_INFO"
        const val DIRECTORY_INFO = "DIRECTORY_INFO"
        const val USER_ID = "USER_ID"
        const val FORGOT_PASSWORD_DATA = "FORGOT_PASSWORD_DATA"
        const val EMAIL = "EMAIL"
        const val NOTIFICATION_TYPE = "NOTIFICATION_TYPE"
        const val IS_FROM_NOTIFICATION = "IS_FROM_NOTIFICATION"
        const val FILTER_TYPE = "FILTER_TYPE"
        const val FILTER_DATA = "FILTER_DATA"
        const val PRODUCT_TYPE = "PRODUCT_TYPE"
        const val PRODUCT_ID = "PRODUCT_ID"
        const val FROM_DYNAMIC_LINK = "FROM_DYNAMIC_LINK"
        const val SELLER_ID = "SELLER_ID"
        const val RC_LOCATION_PERM = 1
        const val LOCATION_SETTING_STATUS = 2
        const val SELECT_CATEGORY = 3
        const val SELECT_SUB_CATEGORY = 4
        const val ADD_JOB = 5
        const val REQUEST_CROP_IMAGE = 6
        const val EXTERNAL_STORAGE_PERMISSION = 7
        const val CATEGORY_ID = "CATEGORY_ID"
        const val CATEGORY_NAME = "CATEGORY_NAME"
        const val SQ_FT_PRICE_TYPE = "SQ_FT_PRICE_TYPE"
        const val WEB_URL = "WEB_URL"
        const val TITLE = "TITLE"
        const val OTP_TYPE = "OTP_TYPE"

    }

    object SharedPrefKey {
        const val USER_INFO = "USER_INFO"
        const val USERS = "USERS"
        const val APP_URL = "APP_URL"
        const val THEME_MODE = "THEME_MODE"
        const val CHAT_USER_INFO = "CHAT_USER_INFO"
        const val DEVICE_ID = "DEVICE_ID"
        const val PRODUCT_CONFIGURATION = "PRODUCT_CONFIGURATION"
        const val COMPARE_PRODUCT_IDS = "COMPARE_PRODUCT_IDS"
        const val INTRODUCTION_SLIDER = "INTRODUCTION_SLIDER"
        const val SUPPORT_NUMBER = "SUPPORT_NUMBER"
    }

    object THEME_MODE {
        const val LIGHT = 0
        const val DARK = 1
    }

    object DialogIdentifier {
        const val LOGOUT = 1
        const val SELECT_COUNTRY_FLAG = 2
        const val SELECT_POST_CODE = 3
        const val CLEAR_CHAT = 4
        const val CLEAR_FILTER = 5
        const val SELECT_CATEGORY = 6
        const val SELECT_GRADE = 7
        const val SELECT_SIZE = 8
        const val SELECT_RATE_TYPE = 9
        const val SELECT_PRICE_TYPE = 10
        const val SELECT_NO_OF_BOXES = 11
        const val SELECT_PRODUCT_SORT = 12
        const val SELECT_SELLER_SORT = 13
        const val SELECT_STATE = 14
        const val DELETE_PRODUCT = 15
        const val SELECT_ISSUE_TYPE = 16
        const val REPORT_PRODUCT_ISSUE_DIALOG = 17
        const val REPORT_SELLER_ISSUE_DIALOG = 18
        const val ADD_PRODUCT_SUCCESS = 19
        const val DELETE_ACCOUNT = 20
        const val DELETE_ACCOUNT_SUCCESS = 21
        const val UPDATE_APP = 22

        const val DOB_PICKER = "DOB_PICKER"
    }

    object LocationMode {
        const val LOCATION_MODE_HIGH_ACCURACY = 1
    }

    object Action {
        const val DELETE_LOGIN_USER = 1
        const val VIEW_LOGIN_USER = 2
        const val WATCHLIST = 3
        const val COMPARE = 4
        const val REMOVE_COMPARE = 5
        const val PRODUCTS_DETAILS = 6
        const val DIRECTORY_DETAILS = 7
        const val EDIT_PRODUCT = 8
        const val DELETE_PRODUCT = 9
        const val REPORT_ISSUE = 10;
        const val SHARE_PRODUCT = 11
        const val SHARE_SELLER = 12
        const val CATEGORY_DETAILS = 13
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
        const val PRODUCTS_LIMIT = 10
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

