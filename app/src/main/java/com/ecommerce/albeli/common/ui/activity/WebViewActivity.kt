package com.ecommerce.albeli.common.ui.activity

import android.content.Context
import android.os.Bundle
import android.webkit.*
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.databinding.ActivityWebviewBinding

class WebViewActivity : BaseActivity() {
    private lateinit var binding: ActivityWebviewBinding
    private var mContext: Context? = null
    private var postUrl: String? = ""
    private var title: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor()
        mContext = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview)
        intentData()
    }

    private fun intentData() {
        if (intent != null && intent.hasExtra(AppConstants.IntentKey.WEB_URL)) {
            postUrl = intent.getStringExtra(AppConstants.IntentKey.WEB_URL)
            title = intent.getStringExtra(AppConstants.IntentKey.TITLE)
            setupToolbar(title, true)
            showProgressDialog(mContext!!,"")
            initWebView()
            binding.webView.loadUrl(postUrl!!)
        }
    }

    private fun initWebView() {
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, weburl: String) {
                hideProgressDialog()
            }
        }
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
//                    hideProgressDialog()
                }
            }
        }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)
    }



}