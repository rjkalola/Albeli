package com.ecommerce.albeliapp.common.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings.Secure
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.ecommerce.imagepickers.models.FileWithPath
import com.ecommerce.imagepickers.utils.Constant
import com.ecommerce.imagepickers.utils.GlideUtil
import com.ecommerce.imagepickers.utils.ImageUtils
import com.ecommerce.albeliapp.MyApplication
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.authentication.data.model.User
import com.ecommerce.albeliapp.authentication.data.model.Users
import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.common.api.model.FcmData
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.ui.activity.WebViewActivity
import com.ecommerce.utilities.callback.DialogButtonClickListener
import com.ecommerce.utilities.utils.AlertDialogHelper
import com.ecommerce.utilities.utils.DateFormatsConstants
import com.ecommerce.utilities.utils.DateHelper
import com.ecommerce.utilities.utils.StringHelper
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object AppUtils {
    fun isLogin(context: Context?): Boolean {
        val user = getUserPreference(context)
        return user != null && user.id > 0
    }

    fun getUserPreference(context: Context?): User? {
        if (context != null && context.applicationContext != null) {
            val gson: Gson = (context.applicationContext as MyApplication).provideGson()
            val userInfo: String =
                MyApplication().preferenceGetString(AppConstants.SharedPrefKey.USER_INFO, "")
            if (!StringHelper.isEmpty(userInfo)) {
                return gson.fromJson(userInfo, User::class.java)
            }
        }
        return null
    }


    fun setUserPreference(context: Context, userInfo: User?) {
        if (context.applicationContext != null) {
            setLoginUsers(context, userInfo!!)
            val gson: Gson =
                (context.applicationContext as MyApplication).provideGson()
            MyApplication().preferencePutString(
                AppConstants.SharedPrefKey.USER_INFO,
                gson.toJson(userInfo)
            )
        }
    }

    fun getDeviceId(context: Context?): String? {
        if (context != null && context.applicationContext != null) {
            val deviceId: String =
                MyApplication().preferenceGetString(AppConstants.SharedPrefKey.DEVICE_ID, "")
            if (!StringHelper.isEmpty(deviceId)) {
                return deviceId
            }
        }
        return null
    }


    fun setDeviceId(context: Context, deviceId: String?) {
        if (context.applicationContext != null) {
            MyApplication().preferencePutString(
                AppConstants.SharedPrefKey.DEVICE_ID,
                deviceId!!
            )
        }
    }

    fun getLoginUsers(context: Context?): Users? {
        if (context != null && context.applicationContext != null) {
            val gson: Gson = (context.applicationContext as MyApplication).provideGson()
            val users: String =
                MyApplication().preferenceGetString(AppConstants.SharedPrefKey.USERS, "")
            if (!StringHelper.isEmpty(users)) {
                return gson.fromJson(users, Users::class.java)
            }
        }
        return null
    }

    fun setLoginUsers(context: Context?, user: User) {
        if (context != null
            && context.applicationContext != null
        ) {
            var users: Users? = null
            if (getLoginUsers(context) != null)
                users = getLoginUsers(context)
            else users = Users()
            if (users?.users != null && users.users.size > 0) {
                for (i in users.users.indices) {
                    val userInfo: User = users.users[i]
                    if (isSameEmail(
                            if (!StringHelper.isEmpty(userInfo.email)) userInfo.email else "",
                            if (!StringHelper.isEmpty(user.email)) user.email else ""
                        )
                    ) {
                        users.users.removeAt(i)
                        break
                    }
                }
            }
            if (users?.users != null && users.users.size > 0) users.users
                .add(0, user) else {
                users?.users!!.add(user)
            }
            val gson: Gson = (context.applicationContext as MyApplication).provideGson()
            MyApplication().preferencePutString(
                AppConstants.SharedPrefKey.USERS,
                gson.toJson(users)
            )
        }
    }

    fun setLoginUsers(context: Context?, users: Users?) {
        if (context != null
            && context.applicationContext != null
        ) {
            val gson: Gson = (context.applicationContext as MyApplication).provideGson()
            MyApplication().preferencePutString(
                AppConstants.SharedPrefKey.USERS,
                gson.toJson(users)
            )
        }
    }

//    fun getChatUserPreference(context: Context?): ChatUsersListResponse? {
//        if (context != null && context.applicationContext != null) {
//            val gson: Gson = (context.applicationContext as MyApplication).provideGson()
//            val userInfo: String =
//                MyApplication().preferenceGetString(AppConstants.SharedPrefKey.CHAT_USER_INFO, "")
//            if (!StringHelper.isEmpty(userInfo)) {
//                return gson.fromJson(userInfo, ChatUsersListResponse::class.java)
//            }
//        }
//        return null
//    }
//
//
//    fun setChatUserPreference(context: Context, chatUsersListResponse: ChatUsersListResponse?) {
//        if (context.applicationContext != null) {
//            val gson: Gson =
//                (context.applicationContext as MyApplication).provideGson()
//            MyApplication().preferencePutString(
//                AppConstants.SharedPrefKey.CHAT_USER_INFO,
//                gson.toJson(chatUsersListResponse)
//            )
//        }
//    }

    fun isSameEmail(email1: String, email2: String): Boolean {
        val address = Pattern.compile("([\\w\\.]+)@([\\w\\.]+\\.\\w+)")
        val match1 = address.matcher(email1)
        val match2 = address.matcher(email2)
        if (!match1.find() || !match2.find()) return false //Not an e-mail address? Already false
        return if (!match1.group(2)
                .equals(match2.group(2), ignoreCase = true)
        ) false else when (match1.group(2).toLowerCase()) {
            "gmail.com" -> {
                val gmail1 = match1.group(1).replace(".", "")
                val gmail2 = match2.group(1).replace(".", "")
                gmail1.equals(gmail2, ignoreCase = true)
            }

            else -> match1.group(1).equals(match2.group(1), ignoreCase = true)
        } //Not same serve? Already false
    }

    fun handleUnauthorized(context: Context?, baseResponse: BaseResponse?) {
        if (context == null || baseResponse == null) return
        if (baseResponse.ErrorCode == AppConstants.UNAUTHORIZED) {
            AlertDialogHelper.showDialog(
                context,
                null,
                context.getString(R.string.msg_unauthorized),
                context.getString(R.string.ok),
                null,
                false, object : DialogButtonClickListener {
                    override fun onPositiveButtonClicked(dialogIdentifier: Int) {
                        if (context is BaseActivity) {
//                            MyApplication().clearData()
//                            context.moveActivity(
//                                context,
//                                DashBoardActivity::class.java, true, true, null
//                            )
                        }
                    }

                    override fun onNegativeButtonClicked(dialogIdentifier: Int) {

                    }
                },
                0
            )
        } else {
            handleResponseMessage(context, baseResponse.Message!!)
        }
    }

    private fun handleResponseMessage(context: Context, messageString: String) {
        try {
            if (context == null) return
            var message: String? = ""
            if (messageString.contains("ERR") || messageString.contains("SUCC")) {
                message = getStringResourceByName(context, messageString)
            } else {
                message = messageString
            }
            AlertDialogHelper.showDialog(
                context, null, message, context.getString(R.string.ok),
                null, false, null, 0
            )
        } catch (e: Exception) {

        }
    }

    private fun getStringResourceByName(mContext: Context?, aString: String?): String {
        try {
            if (mContext == null) return ""
            return if (!StringHelper.isEmpty(aString)) {
                val packageName = mContext.packageName
                var message = mContext.getString(
                    mContext.resources.getIdentifier(
                        aString,
                        "string",
                        packageName
                    )
                )
                if (StringHelper.isEmpty(message)) {
                    message = mContext.getString(R.string.error_unknown)
                }
                message
            } else {
                mContext.getString(R.string.error_unknown)
            }
        } catch (e: Exception) {

        }
        return ""
    }

    fun getHttpErrorMessage(context: Context, statusCode: Int): String? {
        var errorMessage = ""
        when (statusCode) {
            400 -> errorMessage = context.getString(R.string.error_bad_request_400)
            401 -> errorMessage = context.getString(R.string.error_unauthorized_401)
            403 -> errorMessage = context.getString(R.string.error_forbidden_403)
            404 -> errorMessage = context.getString(R.string.error_not_found_404)
            405 -> errorMessage = context.getString(R.string.error_method_not_allowed_405)
            408 -> errorMessage = context.getString(R.string.error_request_timeout_408)
            413 -> errorMessage = context.getString(R.string.error_request_entity_too_large_413)
            414 -> {
                errorMessage = context.getString(R.string.error_request_uri_too_long_414)
                errorMessage = context.getString(R.string.error_request_uri_too_long_414)
            }

            500 -> errorMessage = context.getString(R.string.error_internal_server_error_500)
            else -> errorMessage = context.getString(R.string.error_unknown)
        }
        return errorMessage
    }

    fun getEmptyUserDrawable(mContext: Context): Drawable? {
        return ResourcesCompat.getDrawable(mContext.resources, R.drawable.ic_empty_user, null)
    }

    fun getEmptyGalleryDrawable(mContext: Context): Drawable? {
        return ResourcesCompat.getDrawable(
            mContext.resources,
            R.drawable.ic_empty_image_placeholder,
            null
        )
    }

    fun setUserImage(mContext: Context, image: String?, imageUser: AppCompatImageView) {
        if (!StringHelper.isEmpty(image)) {
            GlideUtil.loadImageUsingGlideTransformation(
                image,
                imageUser,
                Constant.TransformationType.CIRCLECROP_TRANSFORM,
                AppUtils.getEmptyUserDrawable(mContext),
                AppUtils.getEmptyUserDrawable(mContext),
                Constant.ImageScaleType.CENTER_CROP,
                0,
                0,
                "",
                0,
                null
            )
        } else {
            imageUser.setImageDrawable(
                getEmptyUserDrawable(mContext)
            )
        }
    }

    fun setImage(mContext: Context, image: String?, imageView: AppCompatImageView, scaleType: Int) {
        if (!StringHelper.isEmpty(image)) {
            GlideUtil.loadImage(
                image,
                imageView,
                getEmptyGalleryDrawable(mContext),
                getEmptyGalleryDrawable(mContext),
                scaleType,
                null
            )
        } else {
            imageView.setImageDrawable(
                getEmptyGalleryDrawable(mContext)
            )
        }
    }

    fun getApiDateFormat(input: String?): String {
        return if (!StringHelper.isEmpty(input)) {
            DateHelper.changeDateFormat(
                input,
                AppConstants.defaultDateFormat,
                AppConstants.apiDateFormat
            )
        } else {
            ""
        }
    }

    fun getDefaultDateFormat(input: String?): String {
        return if (!StringHelper.isEmpty(input)) {
            DateHelper.changeDateFormat(
                input,
                AppConstants.apiDateFormat,
                AppConstants.defaultDateFormat
            )
        } else {
            ""
        }
    }

    fun getDeviceToken(): String {
        val deviceToken = Secure.getString(
            MyApplication().getContext().contentResolver,
            Secure.ANDROID_ID
        )
        return if (!StringHelper.isEmpty(deviceToken)) {
            deviceToken
        } else {
            ""
        }
    }

    fun getDeviceModel(): String {
        val model = Build.MODEL // returns model name
        return if (!StringHelper.isEmpty(model)) {
            model
        } else {
            ""
        }
    }

    @Throws(IOException::class)
    fun createImageFile(title: String, type: String, imageExtension: String): FileWithPath {
        var imageFileName = ""
        var storageDir: File? = null
        val fileWithPath = FileWithPath()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        if (imageExtension == AppConstants.FileExtension.PDF) {
            imageFileName = "DOCUMENT_" + timeStamp + "_"
            storageDir = File(Environment.getExternalStorageDirectory(), AppConstants.Type.PDF)
        } else {
            if (type == AppConstants.Type.CAMERA) {
                imageFileName = "IMAGE_" + timeStamp + "_"
                storageDir = File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                    ), "Camera"
                )
            } else {
                imageFileName = "IMAGE_" + timeStamp + "_"
                storageDir =
                    File(Environment.getExternalStorageDirectory(), AppConstants.Directory.IMAGES)
            }
        }
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            imageExtension,  /* suffix */
            storageDir /* directory */
        )
        val mCurrentPhotoPath = "file:" + image.absolutePath
        fileWithPath.file = image
        fileWithPath.filePath = mCurrentPhotoPath
        fileWithPath.uri = Uri.fromFile(image)
        return fileWithPath
    }

    fun getFilePathFromBitmap(mContext: Context, bitmap: Bitmap, imageExtension: String): String {
        var filePath = ""
        val filesDir = mContext.filesDir
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFile = File(filesDir, timeStamp + imageExtension)
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            if (imageExtension == AppConstants.FileExtension.PNG) bitmap.compress(
                Bitmap.CompressFormat.PNG,
                100,
                os
            ) else bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            filePath = imageFile.absolutePath
        } catch (e: java.lang.Exception) {

        }
        return filePath
    }

    fun getFileExt(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)
    }

    @Throws(IOException::class)
    fun compressImage(path: String, file: File): FileWithPath? {
        if (file.exists()) {
            val fileWithPath: FileWithPath
            val fileSize = file.length()
            val fileSizeInKB = fileSize / 1024
            if (fileSizeInKB > 1024) {
                fileWithPath = ImageUtils.createImageFile(Environment.DIRECTORY_DCIM)
                fileWithPath.uri = Uri.fromFile(fileWithPath.file)
                ImageUtils.compress(
                    path,
                    fileWithPath.file.absolutePath,
                    AppConstants.MAX_IMAGE_WIDTH,
                    AppConstants.MAX_IMAGE_HEIGHT,
                    AppConstants.IMAGE_QUALITY
                )
                return fileWithPath
            }
        }
        return null
    }

    fun getRequestBody(input: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), input)
    }

    fun formatFileSize(size: Double): String {
        var hrSize: String? = null
        val k = size / 1024.0
        val m = size / 1024.0 / 1024.0
        val g = size / 1024.0 / 1024.0 / 1024.0
        val t = size / 1024.0 / 1024.0 / 1024.0 / 1024.0
        val dec = DecimalFormat("0.00")
        hrSize = if (t > 1) {
            dec.format(t) + " TB"
        } else if (g > 1) {
            dec.format(g) + " GB"
        } else if (m > 1) {
            dec.format(m) + " MB"
        } else if (k > 1) {
            dec.format(k) + " KB"
        } else {
            dec.format(size) + " Bytes"
        }
        return hrSize
    }

    fun convertLongDateToString(longDate: Long, dateFormat: String?): String? {
        try {
            val calendar = Calendar.getInstance()
            //calendar.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            calendar.timeInMillis = longDate
            val df2 = SimpleDateFormat(dateFormat)
            return df2.format(calendar.time)
        } catch (e: java.lang.Exception) {
            Log.e("Error LongDateToString", "" + e)
        }
        return ""
    }

    fun getApplicationVersion(mContext: Context): String {
        var pinfo: PackageInfo? = null
        return try {
            pinfo = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
            pinfo.versionName //versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "0"
        }
    }

    fun opeUrlInBrowser(mContext: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        mContext.startActivity(intent)
    }

    fun setToolbarTextColor(item: MenuItem, title: String, color: Int) {
        val s = SpannableString(title)
        s.setSpan(ForegroundColorSpan(color), 0, s.length, 0)
        item.title = s
    }

    fun getFirebaseUserId(mContext: Context): String {
        return "CUST_" + getUserPreference(mContext)!!.customer_id
    }

    fun getFirebaseTime(date: Date): String {
        val format = SimpleDateFormat(DateFormatsConstants.HH_MM_24, Locale.US)
        return format.format(date)
    }

    fun getFirebaseDate(date: Date): String {
        val format = SimpleDateFormat(DateFormatsConstants.DD_MMMM_YYYY_SPACE, Locale.US)
        var finalDate = ""
        if (DateHelper.isSameDay(date, Date())) {
            finalDate = "Today"
        } else if (DateHelper.isYesterDay(date, Date())) {
            finalDate = "Yesterday"
        } else {
            finalDate = format.format(date)
        }
        return finalDate
    }

    fun checkFirebaseDate(date: Date): String {
        val format = SimpleDateFormat(DateFormatsConstants.DD_MMM_YYYY_SPACE, Locale.US)
        return format.format(date)
    }

    fun showKeyBoard(mContext: Context) {
        val imm: InputMethodManager =
            mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun hideKeyBoard(mContext: Context) {
        val imm =
            mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun getFcmIntent(data: FcmData): Intent? {
        var intent: Intent? = null
        val bundle = Bundle()
        bundle.putString(AppConstants.IntentKey.NOTIFICATION_TYPE, data.type)
        when (data.type) {
            "1" -> intent = Intent("com.ecommerce.albeliapp.dashboard.ui.activity.DashBoardActivity")
            else -> intent = Intent("com.ecommerce.albeliapp.dashboard.ui.activity.DashBoardActivity")
        }
        if (intent != null) {
            intent.putExtras(bundle)
        }
        return intent
    }

    fun shareProductDynamicLink(activity: Activity, productId: Int) {
        val iosParameter: DynamicLink.IosParameters =
            DynamicLink.IosParameters.Builder("com.app.Tile-Bazar").setAppStoreId("6443541980")
                .build()

        val dynamicLink: DynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://owlmanagement.com/productdetails/$productId"))
            .setDomainUriPrefix(AppConstants.DYNAMIC_LINK_PREFIX)
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder().build()
            )
            .setIosParameters(iosParameter)
            .buildDynamicLink()

        val dynamicLinkUri: Uri = dynamicLink.uri
        FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLongLink(dynamicLinkUri)
            .buildShortDynamicLink()
            .addOnCompleteListener(
                activity
            ) { task ->
                if (task.isSuccessful) {
                    val shortLink: Uri = task.result!!.shortLink!!
                    var userName = "I"
                    if (isLogin(activity))
                        userName = getUserPreference(activity)!!.name
                    val shareText =
                        "$userName shared a best deal with you. Please check and get more exclusive deals.\n$shortLink"
                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    intent.putExtra(Intent.EXTRA_TEXT, shareText)
                    intent.type = "text/plain"
                    activity.startActivity(intent)
                }
            }
    }

    fun shareSellerDynamicLink(activity: Activity, sellerId: Int) {
        val iosParameter: DynamicLink.IosParameters =
            DynamicLink.IosParameters.Builder("com.app.Tile-Bazar").setAppStoreId("6443541980")
                .build()
        val dynamicLink: DynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://owlmanagement.com/sellerdetails/$sellerId"))
            .setDomainUriPrefix(AppConstants.DYNAMIC_LINK_PREFIX)
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder().build()
            )
            .setIosParameters(iosParameter)
            .buildDynamicLink()
        val dynamicLinkUri: Uri = dynamicLink.uri
        FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLongLink(dynamicLinkUri)
            .buildShortDynamicLink()
            .addOnCompleteListener(
                activity
            ) { task ->
                if (task.isSuccessful) {
                    val shortLink: Uri = task.result!!.shortLink!!
                    var userName = "I"
                    if (isLogin(activity))
                        userName = getUserPreference(activity)!!.name
                    val shareText =
                        "$userName shared a best deal with you. Please check and get more exclusive deals.\n$shortLink"
                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    intent.putExtra(Intent.EXTRA_TEXT, shareText)
                    intent.type = "text/plain"
                    activity.startActivity(intent)
                }
            }
    }

    fun getApplicationUrlDynamicLink(activity: Activity) {
        val iosParameter: DynamicLink.IosParameters =
            DynamicLink.IosParameters.Builder("com.app.Tile-Bazar").setAppStoreId("6443541980")
                .build()
        val dynamicLink: DynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://owlmanagement.com/"))
            .setDomainUriPrefix(AppConstants.DYNAMIC_LINK_PREFIX)
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder().build()
            )
            .setIosParameters(iosParameter)
            .buildDynamicLink()

        val dynamicLinkUri: Uri = dynamicLink.uri
        FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLongLink(dynamicLinkUri)
            .buildShortDynamicLink()
            .addOnCompleteListener(
                activity
            ) { task ->
                if (task.isSuccessful) {
                    val shortLink: Uri = task.result!!.shortLink!!
                    Log.e("test", "shortLink:" + shortLink);
                    var userName = "I"
                    if (isLogin(activity))
                        userName = getUserPreference(activity)!!.name
                    val shareText =
                        "$userName shared a best deal with you. Please check and get more exclusive deals.\n$shortLink"
//                    val intent = Intent()
//                    intent.action = Intent.ACTION_SEND
//                    intent.putExtra(Intent.EXTRA_TEXT, shareText)
//                    intent.type = "text/plain"
//                    activity.startActivity(intent)
                }
            }
    }

    fun PerfectDecimal(str: String, MAX_BEFORE_POINT: Int, MAX_DECIMAL: Int): String {
        var str = str
        if (str[0] == '.') str = "0$str"
        val max = str.length
        var rFinal = ""
        var after = false
        var i = 0
        var up = 0
        var decimal = 0
        var t: Char
        while (i < max) {
            t = str[i]
            if (t != '.' && after == false) {
                up++
                if (up > MAX_BEFORE_POINT) return rFinal
            } else if (t == '.') {
                after = true
            } else {
                decimal++
                if (decimal > MAX_DECIMAL) return rFinal
            }
            rFinal = rFinal + t
            i++
        }
        return rFinal
    }

    fun capitalizeFirstWordString(str: String): String {
        var retStr = str
        try {
            retStr = str.substring(0, 1).toUpperCase() + str.substring(1)
        } catch (e: Exception) {
        }
        return retStr
    }

    fun capitalizeEveryFirstWordString(str: String): String {
        val str = str.split(" ")
            .joinToString(separator = " ", transform = String::capitalize)
        return str
    }

    fun getPriceTypeName(str: String): String {
        val str = str.replace("per ", "").replace("Per ", "").split(" ")
            .joinToString(separator = " ", transform = String::capitalize)
        return str
    }

    fun shareApplication(mContext: Context) {
        try {
            val appPackageName = mContext.applicationContext.packageName
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name))
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Are you into CERAMICS?\n\nGet the best deals or offer your products at best.\nGet start today and reach millions of buyers and sellers.\n\nAndroid app:\nhttps://play.google.com/store/apps/details?id=$appPackageName\n\niOS app:\nhttps://apps.apple.com/us/app/tile-bazar/id6443541980\n\nDownload TILE BAZAR app and get lowest price guaranteed in any ceramic tile products."
            )
            intent.type = "text/plain"
            mContext.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun appInstalledOrNot(mContext: Context, packageName: String): Boolean {
        return try {
            val pm: PackageManager = mContext.packageManager
            pm.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    fun openWebView(mContext: Context, title: String, url: String) {
        val bundle = Bundle()
        bundle.putString(AppConstants.IntentKey.WEB_URL, url)
        bundle.putString(AppConstants.IntentKey.TITLE, title)
        val intent = Intent(mContext, WebViewActivity::class.java)
        intent.putExtras(bundle)
        mContext.startActivity(intent)
    }

    fun setSellerName(mContext: Context, name: String, textView: TextView) {
        if (!StringHelper.isEmpty(name)) {
            if (isLogin(mContext)) {
                if (AppUtils.getUserPreference(mContext)!!.is_paid == AppConstants.Type.FREE_USER) {
                    if (name.length >= 4) {
                        textView.text = name.substring(0, 4) + getStringWithStart(name.length - 4);
                    } else {
                        textView.text = name
                    }
                } else if (AppUtils.getUserPreference(mContext)!!.is_paid == AppConstants.Type.PREMIUM_USER) {
                    textView.text = name
                }
            } else {
                if (name.length >= 4) {
                    textView.text = name.substring(0, 4) + getStringWithStart(name.length - 4);
                } else {
                    textView.text = name
                }
            }
        }
    }

    fun getStringWithStart(count: Int): String {
        var str = "";
        for (i in 0 until count) {
            str += "*"
        }
        return str;
    }

    fun openPlayStore(context: Context) {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + packageInfo.packageName)
                )
            )
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageInfo!!.packageName)
                )
            )
        }
    }
}