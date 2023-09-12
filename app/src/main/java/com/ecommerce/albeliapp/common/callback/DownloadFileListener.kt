package com.ecommerce.albeliapp.common.callback

interface DownloadFileListener {
    fun onFileDownloaded(directory: String,extension:String, action: Int)
}