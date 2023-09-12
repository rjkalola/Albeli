package com.ecommerce.albeliapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.text.Html
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import com.ecommerce.albeliapp.R
import com.ecommerce.imagepickers.callback.ImageLoadingListener
import com.ecommerce.imagepickers.utils.GlideUtil
import com.ecommerce.albeliapp.common.api.model.FcmData
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.utilities.utils.StringHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService(), ImageLoadingListener {
    var data: FcmData? = null
    var mContext: Context? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        mContext = this

        if (remoteMessage.data.isNotEmpty()){
            Log.d("test", "Message data payload: ${remoteMessage.data}")
            data = FcmData()
            data!!.title = remoteMessage.data["title"]
            data!!.body = remoteMessage.data["body"]
            data!!.imageUrl = remoteMessage.data["imageUrl"]
            data!!.sound = remoteMessage.data["sound"]
            data!!.type = remoteMessage.data["type"]
            data!!.notification_type = remoteMessage.data["notification_type"]
            data!!.timestamp = remoteMessage.data["timestamp"]

            if (!StringHelper.isEmpty(data!!.imageUrl)) {
                getBitmapFromUrl(data!!.imageUrl!!)
            } else {
                sendNotification(data!!, null)
            }
        }

    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }


    private fun sendRegistrationToServer(token: String?) {

    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    private fun sendNotification(data: FcmData, bitmap: Bitmap?) {
        val CHANNEL_ID = resources.getString(R.string.default_notification_channel_id)
        val pendingIntent = redirectToActivity(data)
        if (pendingIntent != null) {
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app_white_120)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(Html.fromHtml(data.title, HtmlCompat.FROM_HTML_MODE_LEGACY)) //                    .setContentTitle(getString(R.string.app_name))
                .setContentText(Html.fromHtml(data.body, HtmlCompat.FROM_HTML_MODE_LEGACY))

            if (bitmap != null) {
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null)
                ).setLargeIcon(bitmap)
            } else {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle().bigText(Html.fromHtml(data.body, HtmlCompat.FROM_HTML_MODE_LEGACY))
                )
            }
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    AppConstants.EXTRA_CHANNEL_SID,
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
            val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
            notificationManager.notify(m /* ID of notification */, notificationBuilder.build())
        }
    }

    private fun redirectToActivity(data: FcmData): PendingIntent? {
        var intent: Intent? = null
        intent = if (AppUtils.getUserPreference(this@MyFirebaseMessagingService) == null) {
            Intent("com.ecommerce.albeliapp.authentication.ui.activity.LoginActivity")
        } else {
            AppUtils.getFcmIntent(data)
        }
        if (intent != null) {
            intent.putExtra(AppConstants.IntentKey.IS_FROM_NOTIFICATION, true)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        return if (intent != null) {
            PendingIntent.getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else null
    }

    private fun getBitmapFromUrl(bitmapURL: String) {
        GlideUtil.getImageBitmap(bitmapURL, mContext, this)
    }

    override fun onLoaded(bitmap: Bitmap?, glideDrawable: Drawable?) {
        if (bitmap != null) {
            sendNotification(data!!, bitmap)
        }
    }

    override fun onFailed(exception: Exception?) {

    }
}