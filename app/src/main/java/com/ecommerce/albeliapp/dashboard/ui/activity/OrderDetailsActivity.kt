package com.ecommerce.albeliapp.dashboard.ui.activity


import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.Html
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductInfo
import com.ecommerce.albeliapp.dashboard.data.model.NotificationInfo
import com.ecommerce.albeliapp.dashboard.data.model.OrderInfo
import com.ecommerce.albeliapp.dashboard.data.model.OrderProductInfo
import com.ecommerce.albeliapp.dashboard.data.model.ProductAttributeInfo
import com.ecommerce.albeliapp.dashboard.data.model.ProductOptionInfo
import com.ecommerce.albeliapp.dashboard.data.model.ProductOptionsItemInfo
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.CheckboxListAdapter
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.DashboardBannerPagerDotsAdapter
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.DashboardCategoryAdapter
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.NotificationListAdapter
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.OrderProductsAdapter
import com.ecommerce.albeliapp.dashboard.ui.adapter.DashboardBannerPagerAdapter
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.ActivityOrderDetailBinding
import com.ecommerce.albeliapp.databinding.ActivityProductDetailsBinding
import com.ecommerce.utilities.utils.AlertDialogHelper
import com.ecommerce.utilities.utils.NetworkHelper
import com.ecommerce.utilities.utils.StringHelper
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Timer
import java.util.TimerTask


class OrderDetailsActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var mContext: Context;
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0
    private var orderId: String = ""
    private var isUpdate = false
    private var orderInfo: OrderInfo = OrderInfo()
    private var adapterOrderProducts: OrderProductsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail)
        setStatusBarColor()
        mContext = this
        mOrderDetailsResponse()
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
            this.lastClickedTime = SystemClock.elapsedRealtime();
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
                        orderInfo = response.Data
                        binding.info = response.Data
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

    override fun onBackPressed() {
        if (isUpdate)
            setResult(Activity.RESULT_OK)
        finish()
    }

}