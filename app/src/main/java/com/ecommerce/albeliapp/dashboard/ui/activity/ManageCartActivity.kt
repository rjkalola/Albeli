package com.ecommerce.albeliapp.dashboard.ui.activity


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.ui.adapter.ViewPagerAdapter
import com.ecommerce.albeliapp.dashboard.data.ui.fragment.AddressListFragment
import com.ecommerce.albeliapp.dashboard.data.ui.fragment.ManageCartFragment
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.ActivityManageCartBinding
import com.ecommerce.utilities.utils.ToastHelper
import com.ecommerce.utilities.utils.ViewPagerDisableSwipe
import org.koin.androidx.viewmodel.ext.android.viewModel


class ManageCartActivity : BaseActivity(), OnClickListener {
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
//                                (pagerAdapter.getmFragmentList()[selectedTabIndex] as AddressListFragment).loadData(true)
                                selectedTabIndex = 2
                                setOrderProgress(selectedTabIndex)
                            } else {
                                ToastHelper.showSnackBar(
                                    mContext, "You have not added any address.", binding.root
                                )
                            }
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
            }

            2 -> {
                selectedTabIndex = 1
                setOrderProgress(selectedTabIndex)
                binding.viewPager.currentItem = selectedTabIndex
            }
        }
    }

}