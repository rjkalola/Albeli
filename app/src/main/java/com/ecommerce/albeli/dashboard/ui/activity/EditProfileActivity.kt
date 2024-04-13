package com.ecommerce.albeli.dashboard.ui.activity


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.ui.activity.BaseActivity
import com.ecommerce.albeli.common.utils.AppUtils
import com.ecommerce.albeli.dashboard.data.model.MyProfileResponse
import com.ecommerce.albeli.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeli.databinding.ActivityEditProfileBinding
import com.ecommerce.utilities.utils.ToastHelper
import com.ecommerce.utilities.utils.ValidationUtil
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditProfileActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0
    private var myProfileResponse: MyProfileResponse = MyProfileResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        setStatusBarColor()
        mContext = this
        myProfileResponse()
        storeProfileResponse()
        binding.imgBack.setOnClickListener(this)
        binding.tvSave.setOnClickListener(this)
        showCustomProgressDialog(binding.progressBarView.routProgress)
        dashboardViewModel.getMyProfileResponse()
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime()
            when (v.id) {
                R.id.imgBack ->
                    onBackPressed()

                R.id.tvSave -> {
                    val firstName: String = binding.edtUserFirstName.text.toString()
                    val lastName: String = binding.edtUserLastName.text.toString()
                    val email: String = binding.edtUserEmail.text.toString()
                    val phone: String = binding.edtUserPhone.text.toString()
                    if (checkValidation(firstName, lastName, email, phone)) {
                        showProgressDialog(mContext, "")
                        dashboardViewModel.storeMyProfileResponse(firstName, lastName, email, phone)
                    }
                }

            }
        }
    }

    private fun checkValidation(
        firstName: String,
        lastName: String,
        email: String,
        phone: String
    ): Boolean {
        return if (TextUtils.isEmpty(firstName)) {
            ToastHelper.showSnackBar(mContext, "Please enter first name.", binding.routBack)
            true
        } else if (TextUtils.isEmpty(lastName)) {
            ToastHelper.showSnackBar(mContext, "Please enter last name.", binding.routBack)
            true
        } else if (TextUtils.isEmpty(email)) {
            ToastHelper.showSnackBar(mContext, "Please enter email address.", binding.routBack)
            false
        } else if (!ValidationUtil.isValidEmail(email)) {
            ToastHelper.showSnackBar(
                mContext,
                "Please enter valid email address.",
                binding.routBack
            )
            false
        } else if (TextUtils.isEmpty(phone)) {
            ToastHelper.showSnackBar(mContext, "Please enter phone number.", binding.routBack)
            false
        } else if (phone.length > 9) {
            true
        } else {
            ToastHelper.showSnackBar(mContext, "Please enter valid phone number.", binding.routBack)
            false
        }
    }


    private fun myProfileResponse() {
        dashboardViewModel.myProfileResponse.observe(this) { response ->
            hideCustomProgressDialog(binding.progressBarView.routProgress)
            try {
                if (response == null) {
                    ToastHelper.showSnackBar(
                        mContext,
                        getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {
                        myProfileResponse = response
                        binding.info = response
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun storeProfileResponse() {
        dashboardViewModel.baseResponse.observe(this) { response ->
            hideProgressDialog()
            try {
                if (response == null) {
                    ToastHelper.showSnackBar(
                        mContext,
                        getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {
                        val firstName: String = binding.edtUserFirstName.text.toString()
                        val lastName: String = binding.edtUserLastName.text.toString()
                        val email: String = binding.edtUserEmail.text.toString()
                        val phone: String = binding.edtUserPhone.text.toString()
                        val user = AppUtils.getUserPreference(mContext)
                        user?.first_name = firstName
                        user?.last_name = lastName
                        user?.email = email
                        user?.phone = phone
                        AppUtils.setUserPreference(mContext, user)
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

}