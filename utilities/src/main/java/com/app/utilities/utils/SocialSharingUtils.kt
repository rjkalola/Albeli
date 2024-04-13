package com.ecommerce.utilities.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.webkit.WebView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object SocialSharingUtils {
    /**
     * @param mContext
     * @param body        ( text to share )
     * @param intentTitle
     */
    fun SocialTextSharing(mContext: Context, body: String?, intentTitle: String?) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, body)
        mContext.startActivity(Intent.createChooser(sharingIntent, intentTitle))
    }

    /**
     * @param mContext
     * @param mBitmap
     * @param intentTitle
     */
    fun SocialImageSharing(mContext: Context, mBitmap: Bitmap, intentTitle: String?) {
        val bytes = ByteArrayOutputStream()
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(mContext.contentResolver, mBitmap, "Title", null)
        val uri = Uri.parse(path)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpg"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        mContext.startActivity(Intent.createChooser(shareIntent, intentTitle))
    }

    /**
     * @param drawable
     * @return
     */
    fun getLocalBitmapUri(drawable: Drawable?): Uri? {
        // Extract Bitmap from ImageView drawable
        // Drawable drawable = imageView.getDrawable();
        var bmp: Bitmap? = null
        bmp = if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            return null
        }

        // Store image to default external storage directory
        var bmpUri: Uri? = null
        try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            file.parentFile.mkdirs()
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    /**
     * @param mContext
     * @param htmlFormate
     * @param intentTitle
     */

}
