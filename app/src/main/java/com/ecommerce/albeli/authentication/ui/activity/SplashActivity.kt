package com.ecommerce.albeli.authentication.ui.activity


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.api.CommonViewModel
import com.ecommerce.albeli.common.ui.activity.BaseActivity
import com.ecommerce.albeli.common.utils.AppUtils
import com.ecommerce.albeli.dashboard.ui.activity.DashboardActivity
import com.ecommerce.albeli.databinding.ActivitySplashBinding
import com.ecommerce.utilities.utils.AlertDialogHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var mContext: Context;
    private val commonViewModel: CommonViewModel by viewModel()
    private val SPLASH_TIME_OUT = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        setStatusBarColor()
        mContext = this

        Handler(Looper.getMainLooper()).postDelayed({
//            val userInfo: String =
//                MyApplication().preferenceGetString(AppConstants.SharedPrefKey.USER_INFO, "")
//            if (StringHelper.isEmpty(userInfo) || userInfo.equals("null", ignoreCase = true)) {
//                moveActivity(mContext, LoginActivity::class.java, true, true, null)
//            } else {
            moveActivity(mContext, DashboardActivity::class.java, true, true, null)
//            }
        }, SPLASH_TIME_OUT.toLong())
    }

    private fun getResourcesResponse() {
        commonViewModel.registerConfigurationResponse.observe(this) { response ->
            hideProgressDialog()
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(
                        mContext, null,
                        mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                        null, false, null, 0
                    )
                } else {
                    if (response.IsSuccess) {

                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }


}