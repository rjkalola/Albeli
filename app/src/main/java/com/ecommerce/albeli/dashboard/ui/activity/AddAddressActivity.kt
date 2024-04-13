package com.ecommerce.albeli.dashboard.ui.activity


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.ui.activity.BaseActivity
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.common.utils.AppUtils
import com.ecommerce.albeli.dashboard.data.model.AddressInfo
import com.ecommerce.albeli.dashboard.data.model.AddressResourcesResponse
import com.ecommerce.albeli.dashboard.data.model.StateInfo
import com.ecommerce.albeli.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeli.databinding.ActivityAddAddressBinding
import com.ecommerce.utilities.utils.NetworkHelper
import com.ecommerce.utilities.utils.StringHelper
import com.ecommerce.utilities.utils.ToastHelper
import com.ecommerce.utilities.utils.ValidationUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.parceler.Parcels


class AddAddressActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityAddAddressBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0
    lateinit var addressInfo: AddressInfo
    lateinit var addressResourcesResponse: AddressResourcesResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address)
        setStatusBarColor()
        mContext = this
        mAddressResourcesResponse()
        mAddAddressResponse()
        binding.imgBack.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        getIntentData()
    }

    private fun getIntentData() {
        if (intent!!.hasExtra(AppConstants.IntentKey.ADDRESS_INFO)
            && Parcels.unwrap<AddressInfo>(intent.getParcelableExtra(AppConstants.IntentKey.ADDRESS_INFO)) != null
        ) {
            binding.txtTitle.text = "Edit Address"
            addressInfo =
                Parcels.unwrap<AddressInfo>(intent.getParcelableExtra(AppConstants.IntentKey.ADDRESS_INFO))
            binding.info = addressInfo
            if (addressInfo.is_default == "1")
                binding.cbDefault.isChecked = true
        } else {
            binding.txtTitle.text = "Add Address"
            addressInfo = AddressInfo()
            val user = AppUtils.getUserPreference(mContext)
            if (user != null) {
                if (!StringHelper.isEmpty(user.first_name))
                    addressInfo.first_name = user.first_name
                if (!StringHelper.isEmpty(user.last_name))
                    addressInfo.last_name = user.last_name
                if (!StringHelper.isEmpty(user.email))
                    addressInfo.email = user.email
            }
            binding.info = addressInfo
        }
        dashboardViewModel.getAddressResourcesResponse()
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime()
            when (v.id) {
                R.id.imgBack ->
                    onBackPressed()

                R.id.btnAddToCart -> {
                    if (!NetworkHelper.isConnected(mContext)) {
                        ToastHelper.showSnackBar(
                            mContext,
                            mContext.getString(R.string.alert_no_connection),
                            binding.root
                        )
                    } else if (AppUtils.isLogin(mContext)) {

                    } else {
                        AppUtils.showLoginRequiredAlertDialog(mContext)
                    }
                }

                R.id.btnSave -> {
                    val address1: String = binding.edtAddress1.text.toString()
                    val address2: String = binding.edtAddress2.text.toString()
                    val city: String = binding.edtCity.text.toString()
                    if (binding.spState.selectedItemPosition == 0) {
                        ToastHelper.showSnackBar(
                            mContext,
                            "Please select valid state.",
                            binding.root
                        )
                        return
                    }
                    val stateKey: String =
                        addressResourcesResponse.Data[binding.spState.selectedItemPosition].short
                    val zipCode: String = binding.edtPinCode.text.toString()
                    val firstName: String = binding.edtFirstName.text.toString()
                    val lastName: String = binding.edtLastName.text.toString()
                    val email: String = binding.edtEmail.text.toString()
                    val phone: String = binding.edtPhone.text.toString()
                    if (checkValidation(
                            address1,
                            address2,
                            city,
                            stateKey,
                            zipCode,
                            firstName,
                            lastName,
                            email,
                            phone
                        )
                    ) {
                        addressInfo.state = stateKey
                        addressInfo.country = "IN"
                        if (binding.cbDefault.isChecked)
                            addressInfo.is_default = "1"
                        else
                            addressInfo.is_default = "0"
                        showProgressDialog(mContext, "")
                        dashboardViewModel.addAddressResponse(addressInfo)
                    }
                }
            }
        }
    }

    private fun mAddressResourcesResponse() {
        dashboardViewModel.addressResourcesResponse.observe(this) { response ->
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
                        addressResourcesResponse = response
                        val stateInfo = StateInfo()
                        stateInfo.value = "Select State"
                        stateInfo.name = "Select State"
                        addressResourcesResponse.Data.add(0, stateInfo)
                        for (i in addressResourcesResponse.Data.indices) {
                            addressResourcesResponse.Data[i].name =
                                addressResourcesResponse.Data[i].value
                        }
                        binding.spState.adapter = ArrayAdapter<StateInfo>(
                            mContext,
                            android.R.layout.simple_list_item_1,
                            addressResourcesResponse.Data
                        )
                        if (addressInfo.id > 0) {
                            for (i in addressResourcesResponse.Data.indices) {
                                if (addressInfo.state == addressResourcesResponse.Data[i].short) {
                                    binding.spState.setSelection(i)
                                    break
                                }
                            }
                        }

                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun mAddAddressResponse() {
        dashboardViewModel.addAddressResponse.observe(this) { response ->
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

    private fun checkValidation(
        address1: String,
        address2: String,
        city: String,
        state: String,
        zipCode: String,
        firstName: String,
        lastName: String,
        email: String,
        phone: String
    ): Boolean {
        return if (TextUtils.isEmpty(address1)) {
            ToastHelper.showSnackBar(mContext, "Please enter address1.", binding.root)
            false
        } else if (TextUtils.isEmpty(city)) {
            ToastHelper.showSnackBar(mContext, "Please enter city.", binding.root)
            false
        } else if (TextUtils.isEmpty(zipCode)) {
            ToastHelper.showSnackBar(mContext, "Please enter zip code.", binding.root)
            false
        } else if (zipCode.length < 6) {
            ToastHelper.showSnackBar(mContext, "Please enter valid zip code.", binding.root)
            false
        } else if (TextUtils.isEmpty(firstName)) {
            ToastHelper.showSnackBar(mContext, "Please enter first name.", binding.root)
            false
        } else if (TextUtils.isEmpty(lastName)) {
            ToastHelper.showSnackBar(mContext, "Please enter last name.", binding.root)
            false
        } else if (TextUtils.isEmpty(email)) {
            ToastHelper.showSnackBar(mContext, "Please enter email address.", binding.root)
            false
        } else if (ValidationUtil.isValidEmail(email)) {
            true
        } else {
            ToastHelper.showSnackBar(mContext, "Please enter valid email address.", binding.root)
            false
        }
    }


}