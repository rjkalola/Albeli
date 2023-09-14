package com.ecommerce.albeliapp.authentication.ui.activity


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.databinding.ActivitySplashBinding
import com.ecommerce.albeliapp.MyApplication
import com.ecommerce.albeliapp.common.api.CommonViewModel
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.utilities.utils.AlertDialogHelper
import com.ecommerce.utilities.utils.StringHelper
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
            val userInfo: String =
                MyApplication().preferenceGetString(AppConstants.SharedPrefKey.USER_INFO, "")
            if (StringHelper.isEmpty(userInfo) || userInfo.equals("null", ignoreCase = true)) {
                if (!MyApplication().preferenceGetBoolean(
                        AppConstants.SharedPrefKey.INTRODUCTION_SLIDER,
                        false
                    )
                )
                    moveActivity(mContext, LoginActivity::class.java, true, true, null)
//                else
//                    moveActivity(mContext, DashBoardActivity::class.java, true, true, null)
            }
//            else {
//                    moveActivity(mContext, DashBoardActivity::class.java, true, true, null)
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
                        AppUtils.handleUnauthorized(mContext, response)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }


}