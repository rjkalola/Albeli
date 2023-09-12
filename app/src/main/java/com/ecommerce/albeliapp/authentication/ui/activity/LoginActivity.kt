package com.ecommerce.albeliapp.authentication.ui.activity


import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.databinding.ActivityLoginBinding
import com.ecommerce.albeliapp.common.api.CommonViewModel
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.utilities.utils.AlertDialogHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mContext: Context;
    private val commonViewModel: CommonViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setStatusBarColor()
        mContext = this
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