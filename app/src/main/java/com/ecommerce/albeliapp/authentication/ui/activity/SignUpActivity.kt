package com.ecommerce.albeliapp.authentication.ui.activity


import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.authentication.ui.viewmodel.AuthenticationViewModel
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.ui.activity.DashboardActivity
import com.ecommerce.albeliapp.databinding.ActivitySignUpBinding
import com.ecommerce.utilities.utils.AlertDialogHelper
import com.ecommerce.utilities.utils.ToastHelper
import com.ecommerce.utilities.utils.ValidationUtil
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mContext: Context
    private val authenticationViewModel: AuthenticationViewModel by viewModel()
    private var lastClickedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        setStatusBarColor()
        mContext = this
        mUserResponse()
        binding.txtSignIn.setOnClickListener(this)
        binding.txtSignUp.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime()
            when (v.id) {
                R.id.txtSignIn ->
                    moveActivity(mContext, LoginActivity::class.java, true, false, null)

                R.id.imgBack ->
                    finish()

                R.id.txtSignUp -> {
                    val firstName: String = binding.edtFirstName.text.toString()
                    val lastName: String = binding.edtLastName.text.toString()
                    val email: String = binding.edtEmail.text.toString()
                    val password: String = binding.edtPassword.text.toString()
                    val confirmPassword: String = binding.edtPassword.text.toString()
                    if (checkValidation(
                            firstName,
                            lastName,
                            email,
                            password,
                            confirmPassword
                        )
                    ) {
                        showProgressDialog(mContext, "")
                        authenticationViewModel.register(firstName, lastName, email, password)
                    }
                }
            }
        }
    }

    private fun checkValidation(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return if (TextUtils.isEmpty(firstName)) {
            ToastHelper.showSnackBar(mContext, "Please enter first name.", binding.root)
            false
        } else if (TextUtils.isEmpty(lastName)) {
            ToastHelper.showSnackBar(mContext, "Please enter last name.", binding.root)
            false
        } else if (TextUtils.isEmpty(email)) {
            ToastHelper.showSnackBar(mContext, "Please enter email address.", binding.root)
            false
        } else if (!ValidationUtil.isValidEmail(email)) {
            ToastHelper.showSnackBar(mContext, "Please enter valid email address.", binding.root)
            false
        } else if (TextUtils.isEmpty(password)) {
            ToastHelper.showSnackBar(mContext, "Please enter password.", binding.root)
            false
        } else if (TextUtils.isEmpty(confirmPassword)) {
            ToastHelper.showSnackBar(mContext, "Please enter Confirm password.", binding.root)
            false
        } else if (password == confirmPassword) {
            true
        } else {
            ToastHelper.showSnackBar(
                mContext,
                "Password and Confirm Password must be same.",
                binding.root
            )
            false
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

}