package com.ecommerce.albeliapp.dashboard.ui.activity


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.data.model.AddressResourcesResponse
import com.ecommerce.albeliapp.dashboard.data.model.OrderInfo
import com.ecommerce.albeliapp.dashboard.data.model.OrderProductInfo
import com.ecommerce.albeliapp.dashboard.data.model.StateInfo
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.OrderProductsAdapter
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.ActivityOrderDetailBinding
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


class OrderDetailsActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0
    private var orderId: String = ""
    private var isUpdate = false
    private var orderInfo: OrderInfo = OrderInfo()
    private var adapterOrderProducts: OrderProductsAdapter? = null
    lateinit var addressResourcesResponse: AddressResourcesResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail)
        setStatusBarColor()
        mContext = this
        mOrderDetailsResponse()
        mAddressResourcesResponse()
        binding.imgBack.setOnClickListener(this)
        getIntentData()
    }

    private fun getIntentData() {
        if (intent.extras != null && intent.hasExtra(AppConstants.IntentKey.ORDER_ID)) {
            orderId = intent.extras!!.getString(AppConstants.IntentKey.ORDER_ID, "")
            showCustomProgressDialog(binding.progressBarView.routProgress)
            dashboardViewModel.mOrderDetailsResponse(orderId)
        }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime()
            when (v.id) {
                R.id.imgBack ->
                    onBackPressed()
            }
        }

    }

    private fun setAdapter(list: MutableList<OrderProductInfo>?) {
        if (list != null && list.size > 0) {
            binding.rvProductInfo.visibility = View.VISIBLE
            adapterOrderProducts = OrderProductsAdapter(mContext, list)
            binding.rvProductInfo.adapter = adapterOrderProducts
            val layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            binding.rvProductInfo.layoutManager = layoutManager
        } else {
            binding.rvProductInfo.visibility = View.GONE
        }
    }

    private fun mOrderDetailsResponse() {
        dashboardViewModel.mOrderDetailsResponse.observe(this) { response ->
//            hideCustomProgressDialog(binding.progressBarView.routProgress)
            try {
                if (response == null) {
                    ToastHelper.showSnackBar(
                        mContext,
                        getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {
                        orderInfo = response.Data
                        binding.info = response.Data
                        dashboardViewModel.getAddressResourcesResponse()
                        setAdapter(response.Data.products)
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
                        fillUI()
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun getFullCountyName(str: String): String {
        return if (str.equals("IN", ignoreCase = true)) "India" else str
    }

    private fun getFullStateName(str: String):String{
        var state = ""
        for (i in addressResourcesResponse.Data.indices) {
            if (str == addressResourcesResponse.Data[i].short) {
                state = addressResourcesResponse.Data[i].value
                break
            }
        }
        return state
    }

    /* access modifiers changed from: private */
    fun fillUI() {
        binding.tvTelephoneValue.text = getOrFillData(orderInfo.customer_phone)
        binding.tvEmailValue.text = getOrFillData(orderInfo.customer_email)
        binding.tvDateValue.text = orderInfo.created_at
        binding.tvShippingValue.text = orderInfo.shipping_method
        binding.tvPaymentValue.text = orderInfo.payment_method
        binding.tvBillingAddressValue.text = getBillingAddress()
        binding.tvShippingAddressValue.text = getShippingAddress()
        binding.tvSubTotal.text = orderInfo.sub_total
        binding.tvFreeShipping.text = orderInfo.shipping_cost
        binding.tvTotal.text = orderInfo.total
    }

    private fun getShippingAddress(): String {
        val sb = StringBuilder()
        sb.append(orderInfo.shipping_first_name + " " + orderInfo.shipping_last_name)
        sb.append("\n")
        sb.append(orderInfo.shipping_address_1)
        sb.append("\n")
        sb.append(orderInfo.shipping_address_2)
        sb.append("\n")
        sb.append((orderInfo.shipping_city + ", " + getFullStateName(orderInfo.shipping_state)).toString() + " " + orderInfo.shipping_zip)
        sb.append("\n")
        sb.append(getFullCountyName(orderInfo.shipping_country))
        return sb.toString()
    }

    private fun getBillingAddress(): String {
        val sb = StringBuilder()
        sb.append(orderInfo.billing_first_name + " " + orderInfo.billing_last_name)
        sb.append("\n")
        sb.append(orderInfo.billing_address_1)
        sb.append("\n")
        sb.append(orderInfo.billing_address_2)
        sb.append("\n")
        sb.append((orderInfo.billing_city + ", " + getFullStateName(orderInfo.billing_state)).toString() + " " + orderInfo.billing_zip)
        sb.append("\n")
        sb.append(getFullCountyName(orderInfo.billing_country))
        return sb.toString()
    }

    private fun getOrFillData(str: String): String {
        return if (TextUtils.isEmpty(str)) "-" else str
    }

    override fun onBackPressed() {
        if (isUpdate)
            setResult(Activity.RESULT_OK)
        finish()
    }

}