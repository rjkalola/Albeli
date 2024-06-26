package com.ecommerce.albeli.authentication.ui.activity


import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeli.R
import com.ecommerce.albeli.authentication.ui.viewmodel.AuthenticationViewModel
import com.ecommerce.albeli.common.ui.activity.BaseActivity
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.common.utils.AppUtils
import com.ecommerce.albeli.dashboard.ui.activity.DashboardActivity
import com.ecommerce.albeli.databinding.ActivityLoginBinding
import com.ecommerce.utilities.utils.AlertDialogHelper
import com.ecommerce.utilities.utils.ToastHelper
import com.ecommerce.utilities.utils.ValidationUtil
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mContext: Context;
    private val authenticationViewModel: AuthenticationViewModel by viewModel()
    private var lastClickedTime: Long = 0
    private var isFromDashboard = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setStatusBarColor()
        mContext = this
        mUserResponse()
        binding.txtSignIn.setOnClickListener(this)
        binding.txtSignUp.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        if (intent.extras != null && intent.hasExtra(AppConstants.IntentKey.FROM_DASHBOARD))
            isFromDashboard = intent.getBooleanExtra(AppConstants.IntentKey.FROM_DASHBOARD, false)
        if (isFromDashboard) {
            binding.routSkipForNow.visibility = View.GONE
            binding.txtSkipNow.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime();
            when (v.id) {
                R.id.txtSignIn -> {
                    val email: String = binding.edtEmail.text.toString().trim()
                    val password: String = binding.edtPassword.text.toString().trim()
                    if (checkValidation(email, password)) {
                        showProgressDialog(mContext, "")
                        authenticationViewModel.login(email, password)
                    }
                }

                R.id.txtSignUp ->
                    moveActivity(mContext, SignUpActivity::class.java, true, false, null)

                R.id.imgBack ->
                    finish()
            }
        }

    }

    private fun mUserResponse() {
        authenticationViewModel.mUserResponse.observe(this) { response ->
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
                        hideProgressDialog()
                        AppUtils.setUserPreference(mContext, response.info)
                        moveActivity(mContext, DashboardActivity::class.java, true, true, null)
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun checkValidation(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            ToastHelper.showSnackBar(mContext, "Please enter email address.", binding.root)
            false
        } else if (!ValidationUtil.isValidEmail(email)) {
            ToastHelper.showSnackBar(mContext, "Please enter valid email address.", binding.root)
            false
        } else if (!TextUtils.isEmpty(password)) {
            true
        } else {
            ToastHelper.showSnackBar(mContext, "Please enter password.", binding.root)
            false
        }
    }
}