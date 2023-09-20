package com.ecommerce.albeliapp.dashboard.ui.activity


import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductInfo
import com.ecommerce.albeliapp.dashboard.data.model.ProductAttributeInfo
import com.ecommerce.albeliapp.dashboard.data.model.ProductOptionInfo
import com.ecommerce.albeliapp.dashboard.data.model.ProductOptionsItemInfo
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.CheckboxListAdapter
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.DashboardBannerPagerDotsAdapter
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.DashboardCategoryAdapter
import com.ecommerce.albeliapp.dashboard.ui.adapter.DashboardBannerPagerAdapter
import com.ecommerce.albeliapp.dashboard.ui.fragment.SelectItemBottomSheetDialog
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.ActivityProductDetailsBinding
import com.ecommerce.utilities.utils.AlertDialogHelper
import com.ecommerce.utilities.utils.CollectionUtils
import com.ecommerce.utilities.utils.StringHelper
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Timer
import java.util.TimerTask


class ProductDetailsActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var mContext: Context;
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0
    private var productId: String = ""
    private var productDetails: CategoryProductInfo = CategoryProductInfo()
    private lateinit var adapterBannerDots: DashboardBannerPagerDotsAdapter
    private var adapterCategoryProduct: DashboardCategoryAdapter? = null
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    val handler = Handler(Looper.getMainLooper())
    private var adapterCheckBox: CheckboxListAdapter? = null
    private var listSelectedOptions: MutableList<ProductOptionsItemInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        setStatusBarColor()
        mContext = this
        mProductDetailsResponse()
        binding.imgBack.setOnClickListener(this)
        binding.btnAddToCart.setOnClickListener(this)

        getIntentData()
    }

    private fun getIntentData() {
        if (intent.extras != null && intent.hasExtra(AppConstants.IntentKey.PRODUCT_ID)) {
            productId = intent.extras!!.getString(AppConstants.IntentKey.PRODUCT_ID, "")
            showCustomProgressDialog(binding.progressBarView.routProgress)
            dashboardViewModel.getProductDetailsResponse(productId)
        }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime();
            when (v.id) {
                R.id.imgBack ->
                    finish()

                R.id.btnAddToCart -> {
                    if (validate()) {
                        Log.e("test", "valid")
                    } else {
                        ToastHelper.showSnackBar(mContext, "Please select any Size.", binding.root)
                    }
                }
            }
        }

    }

    private fun setSliderAdapter(list: List<String>?) {
        if (!list.isNullOrEmpty()) {
            binding.vpBanner1.visibility = View.VISIBLE
            if (list.size > 1)
                binding.tblPageIndicator1.visibility = View.VISIBLE
            else
                binding.tblPageIndicator1.visibility = View.GONE
            val adapterPager =
                DashboardBannerPagerAdapter(
                    mContext,
                    list
                )
            binding.vpBanner1.adapter = adapterPager
            binding.vpBanner1.addOnPageChangeListener(object :
                ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    adapterBannerDots.setSelectedDot(
                        position
                    )
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })

            val linearLayoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            binding.tblPageIndicator1.layoutManager = linearLayoutManager
            adapterBannerDots =
                DashboardBannerPagerDotsAdapter(mContext, list.size)
            binding.tblPageIndicator1.adapter = adapterBannerDots

            if (list.size > 1)
                startTimer()
        } else {
            binding.vpBanner1.visibility = View.GONE
            binding.tblPageIndicator1.visibility = View.GONE

            stopTimer()
        }
    }

    private fun mProductDetailsResponse() {
        dashboardViewModel.productDetailsResponse.observe(this) { response ->
            hideCustomProgressDialog(binding.progressBarView.routProgress)
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(
                        mContext, null,
                        mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                        null, false, null, 0
                    )
                } else {
                    if (response.IsSuccess) {
                        productDetails = response.Data
                        binding.info = response.Data

                        binding.txtOldPrice.paintFlags =
                            binding.txtOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                        if (!StringHelper.isEmpty(productDetails.sku))
                            binding.tvSKU.text = "SKU: " + productDetails.sku

                        binding.tvAvgReview.text = "${productDetails.review_average} out of 5"
                        binding.tvReviewCount.text = productDetails.total_review + " reviews";
                        binding.txtDescription.text = Html.fromHtml(
                            productDetails.product_description,
                            Html.FROM_HTML_MODE_LEGACY
                        );
                        binding.imgFavorite.isChecked = productDetails.wishlisted;
                        val sb = StringBuilder()
                        if (productDetails.product_attributes.isNotEmpty()) {
                            val it: Iterator<ProductAttributeInfo> =
                                productDetails.product_attributes.iterator()
                            while (it.hasNext()) {
                                val next: ProductAttributeInfo = it.next()
                                sb.append(next.name)
                                sb.append("\n")
                                if (next.values.isNotEmpty()) {
                                    val it2: Iterator<String> = next.values.iterator()
                                    var i = 0
                                    while (it2.hasNext()) {
                                        val next2 = it2.next()
                                        if (i != 0) {
                                            sb.append(", ")
                                        }
                                        i++
                                        sb.append(next2)
                                    }
                                }
                                sb.append("\n\n")
                            }
                        }
                        binding.tvDescAdditional.text = sb
                        setSliderAdapter(productDetails.additional_images)
                        setProductOptions()
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun setProductOptions() {
        if (productDetails.product_options.isNotEmpty()) {
            for (i in 0 until productDetails.product_options.size) {
                val info: ProductOptionInfo =
                    productDetails.product_options[i]
                when (info.name) {
                    "radio" -> {
                        binding.routRadioButtonView.visibility = View.VISIBLE
                        binding.tvRGTitle.text = info.opt_value
                        val rb = arrayOfNulls<RadioButton>(info.values.size)
                        for (j in 0 until info.values.size) {
                            rb[j] = RadioButton(this)
                            rb[j]!!.text = info.values[j].label
                            rb[j]!!.id = j
                            binding.radioGroup.addView(rb[j])
                        }
                    }

                    "multiple_select", "checkbox" -> {
                        binding.routCheckboxView.visibility = View.VISIBLE
                        binding.tvCheckBoxTitle.text = info.opt_value
                        setCheckboxAdapter(info.values)
                    }

                    "dropdown" -> {
                        binding.routDropdown.visibility = View.VISIBLE
                        binding.tvDropdownTitle.text = info.opt_value
                        val list: MutableList<String> = ArrayList()
                        list.add("Please select any option")
                        for (j in 0 until info.values.size) {
                            list.add(info.values[j].label)
                        }
                        val adapter = ArrayAdapter(
                            this,
                            android.R.layout.simple_spinner_item, list
                        )
                        binding.spDropdown.adapter = adapter
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        var valid = false
        listSelectedOptions.clear()
        if (productDetails.product_options.isNotEmpty()) {
            for (i in 0 until productDetails.product_options.size) {
                val info: ProductOptionInfo =
                    productDetails.product_options[i]
                when (info.name) {
                    "radio" -> {
                        val id: Int = binding.radioGroup.checkedRadioButtonId
                        if (id != -1) {
                            valid = true
                            listSelectedOptions.add(info.values[id])
                            break
                        }
                    }

                    "multiple_select", "checkbox" -> {
                        if (adapterCheckBox != null) {
                            var check = false;
                            val listIds: MutableList<String> = ArrayList()
                            for (j in 0 until adapterCheckBox?.list!!.size) {
                                if (adapterCheckBox?.list!![j].check) {
                                    listIds.add(adapterCheckBox?.list!![j].id.toString())
                                    check = true
                                    listSelectedOptions.add(info.values[j])
                                }
                            }
                            valid = check
                            if (check)
                                break
                        }
                    }

                    "dropdown" -> {
                        if (binding.spDropdown.selectedItemPosition > 0) {
                            Log.e("test", "Not Checked")
                            valid = true
                            listSelectedOptions.add(info.values[binding.spDropdown.selectedItemPosition - 1])
                            break
                        }
                    }
                }
            }
        }
        return valid
    }

    private fun setCheckboxAdapter(list: MutableList<ProductOptionsItemInfo>) {
        if (list.isNotEmpty()) {
            binding.rvCheckBox.visibility = View.VISIBLE
            adapterCheckBox = CheckboxListAdapter(mContext, list)
            val layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            binding.rvCheckBox.layoutManager = layoutManager
            binding.rvCheckBox.adapter = adapterCheckBox
        } else {
            binding.rvCheckBox.visibility = View.GONE
        }
    }

    private fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    if (binding.vpBanner1.currentItem == productDetails.additional_images.size - 1) {
                        binding.vpBanner1.currentItem = 0
                    } else {
                        binding.vpBanner1.currentItem =
                            binding.vpBanner1.currentItem + 1
                    }
                }
            }
        }
    }

    private fun startTimer() {
        stopTimer()
        timer = Timer()
        initializeTimerTask()
        timer!!.schedule(timerTask, 5000, 5000)
    }

    private fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

}