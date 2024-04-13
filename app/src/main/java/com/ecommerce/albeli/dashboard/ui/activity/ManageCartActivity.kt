package com.ecommerce.albeli.dashboard.ui.activity


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.ui.activity.BaseActivity
import com.ecommerce.albeli.common.ui.adapter.ViewPagerAdapter
import com.ecommerce.albeli.common.utils.AppUtils
import com.ecommerce.albeli.dashboard.data.ui.fragment.AddressListFragment
import com.ecommerce.albeli.dashboard.data.ui.fragment.ManageCartFragment
import com.ecommerce.albeli.dashboard.data.ui.fragment.PaymentFragment
import com.ecommerce.albeli.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeli.databinding.ActivityManageCartBinding
import com.ecommerce.utilities.utils.StringHelper
import com.ecommerce.utilities.utils.ToastHelper
import com.ecommerce.utilities.utils.ViewPagerDisableSwipe
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ManageCartActivity : BaseActivity(), OnClickListener, PaymentResultListener {
    private lateinit var binding: ActivityManageCartBinding
    private lateinit var mContext: Context;
    private lateinit var pagerAdapter: ViewPagerAdapter
    private var selectedTabIndex: Int = 0
    private var doubleBackToExitPressedOnce = false
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0
    private val SEEKBAR_VALUES = intArrayOf(16, 50, 85)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_cart)
        setStatusBarColor()
        mContext = this
        binding.routBack.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)
        setupViewPager(binding.viewPager);
        setOrderProgress(selectedTabIndex)
        binding.sbProgress.setOnTouchListener { v, event -> true }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime();
            when (v.id) {
                R.id.routBack -> {
                    onBackPressed()
                }

                R.id.btnContinue -> {
                    when (selectedTabIndex) {
                        0 -> {
                            if ((pagerAdapter.getmFragmentList()[selectedTabIndex] as ManageCartFragment).validate()) {
                                selectedTabIndex = 1
                                setOrderProgress(selectedTabIndex)
                                binding.viewPager.currentItem = selectedTabIndex
                                (pagerAdapter.getmFragmentList()[selectedTabIndex] as AddressListFragment).loadData(
                                    true
                                )
                            } else {
                                ToastHelper.showSnackBar(
                                    mContext, "Your cart is empty.", binding.root
                                )
                            }
                        }

                        1 -> {
                            if ((pagerAdapter.getmFragmentList()[selectedTabIndex] as AddressListFragment).validate()) {
                                if ((pagerAdapter.getmFragmentList()[selectedTabIndex] as AddressListFragment).isDefaultAddress()) {
                                    selectedTabIndex = 2
                                    setOrderProgress(selectedTabIndex)
                                    binding.viewPager.currentItem = selectedTabIndex
                                    setContinueButtonText(getString(R.string.make_payment))
                                    val addressId =
                                        (pagerAdapter.getmFragmentList()[1] as AddressListFragment).getAddressId()
                                    val totalItems =
                                        (pagerAdapter.getmFragmentList()[0] as ManageCartFragment).getTotalItems()
                                    val totalAmount =
                                        (pagerAdapter.getmFragmentList()[0] as ManageCartFragment).getTotalAmount()
                                    val productInfo =
                                        (pagerAdapter.getmFragmentList()[0] as ManageCartFragment).getProductInfo()
                                    (pagerAdapter.getmFragmentList()[2] as PaymentFragment).initialLoad(
                                        addressId,
                                        totalItems,
                                        totalAmount,
                                        productInfo
                                    )
                                } else {
                                    ToastHelper.showSnackBar(
                                        mContext, "Please select any address.", binding.root
                                    )
                                }
                            } else {
                                ToastHelper.showSnackBar(
                                    mContext, "You have not added any address.", binding.root
                                )
                            }
                        }

                        2 -> {
                            (pagerAdapter.getmFragmentList()[selectedTabIndex] as PaymentFragment).onclickPayment()
                        }
                    }
                }
            }
        }
    }

    fun setOrderProgress(index: Int) {
        binding.sbProgress.progress = SEEKBAR_VALUES[index]
    }

    private fun setupViewPager(viewPager: ViewPagerDisableSwipe) {
        viewPager.setPagingEnabled(false)
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        pagerAdapter.addFrag(ManageCartFragment.newInstance(), "")
        pagerAdapter.addFrag(AddressListFragment.newInstance(), "")
        pagerAdapter.addFrag(PaymentFragment.newInstance(), "")
        viewPager.adapter = pagerAdapter
//        setupTab(selectedTabIndex)

    }

    private fun setupTab(position: Int) {
        selectedTabIndex = position
        resetTabImage()
        binding.viewPager.currentItem = position

//        when (position) {
//            0 -> {
//                if (pagerAdapter.getmFragmentList()[position] is HomeFragment)
//                    (pagerAdapter.getmFragmentList()[position] as HomeFragment).initialDataLoad()
//            }
//
//            1 -> {
//                if (pagerAdapter.getmFragmentList()[position] is CategoryProductsFragment)
//                    (pagerAdapter.getmFragmentList()[position] as CategoryProductsFragment).initialDataLoad()
//            }
//
//            2 -> {
//                if (pagerAdapter.getmFragmentList()[position] is WishlistProductsFragment)
//                    (pagerAdapter.getmFragmentList()[position] as WishlistProductsFragment).initialDataLoad()
//            }
//        }

    }

    private fun resetTabImage() {
//        bindingContent.imgHomeTab.setImageResource(R.drawable.ic_home)
//        bindingContent.imCategoryTab.setImageResource(R.drawable.ic_shirt)
//        bindingContent.imgWatchlistTab.setColorFilter(resources.getColor(R.color.colorBlack))
    }

    /* fun refreshCategoryProducts(categoryId: String, categoryName: String) {
         selectedTabIndex = 1
         resetTabImage()
         bindingContent.viewPager.currentItem = selectedTabIndex
         bindingContent.imCategoryTab.setImageResource(R.drawable.ic_shirt_selected)
         if (pagerAdapter.getmFragmentList()[1] is CategoryProductsFragment)
             (pagerAdapter.getmFragmentList()[1] as CategoryProductsFragment).loadDataFromDashBoard(
                 categoryId,
                 categoryName
             )
     }*/

    override fun onBackPressed() {
        when (selectedTabIndex) {
            0 -> {
                if ((pagerAdapter.getmFragmentList()[selectedTabIndex] as ManageCartFragment).isUpdate())
                    setResult(Activity.RESULT_OK)
                finish()
            }

            1 -> {
                selectedTabIndex = 0
                setOrderProgress(selectedTabIndex)
                binding.viewPager.currentItem = selectedTabIndex
                setContinueButtonText(getString(R.string.action_continue))
            }

            2 -> {
                selectedTabIndex = 1
                setOrderProgress(selectedTabIndex)
                binding.viewPager.currentItem = selectedTabIndex
                setContinueButtonText(getString(R.string.action_continue))
            }
        }
    }

    fun setContinueButtonText(text: String) {
        binding.btnContinue.text = text
    }

    fun startPayment(totalAmount:String) {
        Log.e("test", "startPayment")
        val activity: Activity = this

        val co = Checkout()
        co.setKeyID(getString(R.string.razorKey))
        try {
            val totalPayment = totalAmount.toFloat()
            val options = JSONObject()
            options.put("name", getString(R.string.app_name))
            options.put("description", "Payment")
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
//            options.put("amount", "1000")
            Log.e("test","totalPayment:"+totalPayment)
            Log.e("test","totalAmount:"+totalAmount)
            options.put("amount", totalPayment*100f)
//            options.put("send_sms_hash", true);

            val prefill = JSONObject()
            prefill.put("email", AppUtils.getUserPreference(mContext)?.email)
            prefill.put("contact", AppUtils.getUserPreference(mContext)?.phone)

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            ToastHelper.showSnackBar(
                mContext,
                "Error in payment: " + e.message,
                binding.root
            )
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Log.e("test", "Payment Successful : Payment ID: $p0")
        if (pagerAdapter.getmFragmentList()[selectedTabIndex] is PaymentFragment && !StringHelper.isEmpty(
                p0
            )
        )
            (pagerAdapter.getmFragmentList()[selectedTabIndex] as PaymentFragment).callOrderAPI(
                1,
                p0!!
            )
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.e("test", "Payment Failed : Payment Data: ${p1}")
    }
}