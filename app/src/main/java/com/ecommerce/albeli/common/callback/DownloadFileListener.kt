package com.ecommerce.albeli.common.callback

interface DownloadFileListener {
    fun onFileDownloaded(directory: String,extension:String, action: Int)
}