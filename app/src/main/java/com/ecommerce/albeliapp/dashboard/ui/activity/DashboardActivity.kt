package com.ecommerce.albeliapp.dashboard.ui.activity


import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.authentication.ui.activity.SignUpActivity
import com.ecommerce.albeliapp.common.api.CommonViewModel
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.ui.adapter.ViewPagerAdapter
import com.ecommerce.albeliapp.dashboard.data.ui.fragment.CategoryProductsFragment
import com.ecommerce.albeliapp.dashboard.data.ui.fragment.HomeFragment
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.ActivityDashboardBinding
import com.ecommerce.albeliapp.databinding.ContentDashboardBinding
import com.ecommerce.utilities.utils.ViewPagerDisableSwipe
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var bindingContent: ContentDashboardBinding
    private lateinit var mContext: Context;
    private lateinit var pagerAdapter: ViewPagerAdapter
    private var selectedTabIndex: Int = 0
    private var doubleBackToExitPressedOnce = false
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        bindingContent = DataBindingUtil.bind(binding.appBarLayout.contentDashboard.root)!!
        setStatusBarColor()
        mContext = this
        bindingContent.routHomeTab.setOnClickListener(this)
        bindingContent.routCategoryTab.setOnClickListener(this)
        setupViewPager(bindingContent.viewPager);
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime();
            when (v.id) {
                R.id.routHomeTab -> {
                    setupTab(0)
                }

                R.id.routCategoryTab ->
                    setupTab(1)
            }
        }

    }

    private fun setupViewPager(viewPager: ViewPagerDisableSwipe) {
        viewPager.setPagingEnabled(false)
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        pagerAdapter.addFrag(HomeFragment.newInstance(), "")
        pagerAdapter.addFrag(CategoryProductsFragment.newInstance(), "")
        viewPager.adapter = pagerAdapter
//        setupTab(selectedTabIndex)

    }

    private fun setupTab(position: Int) {
        selectedTabIndex = position
        resetTabImage()
        bindingContent.viewPager.currentItem = position

        when (position) {
            0 -> {
                bindingContent.imgHomeTab.setImageResource(R.drawable.ic_home_selected)
                if (pagerAdapter.getmFragmentList()[position] is HomeFragment)
                    (pagerAdapter.getmFragmentList()[position] as HomeFragment).initialDataLoad()
            }

            1 -> {
                bindingContent.imCategoryTab.setImageResource(R.drawable.ic_shirt_selected)
                if (pagerAdapter.getmFragmentList()[position] is CategoryProductsFragment)
                    (pagerAdapter.getmFragmentList()[position] as CategoryProductsFragment).initialDataLoad()
            }
        }

    }

    private fun resetTabImage() {
        bindingContent.imgHomeTab.setImageResource(R.drawable.ic_home)
        bindingContent.imCategoryTab.setImageResource(R.drawable.ic_shirt)
    }

    fun refreshCategoryProducts(categoryId: String, categoryName: String){
        selectedTabIndex = 1
        resetTabImage()
        bindingContent.viewPager.currentItem = selectedTabIndex
        bindingContent.imCategoryTab.setImageResource(R.drawable.ic_shirt_selected)
        if (pagerAdapter.getmFragmentList()[1] is CategoryProductsFragment)
            (pagerAdapter.getmFragmentList()[1] as CategoryProductsFragment).loadDataFromDashBoard(categoryId,categoryName)
    }

}