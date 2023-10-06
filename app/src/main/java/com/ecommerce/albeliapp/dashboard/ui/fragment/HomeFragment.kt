package com.ecommerce.albeliapp.dashboard.data.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.ecommerce.albeliapp.MyApplication
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.authentication.data.model.User
import com.ecommerce.albeliapp.common.api.model.ModuleInfo
import com.ecommerce.albeliapp.common.callback.SelectItemListener
import com.ecommerce.albeliapp.common.ui.fragment.BaseFragment
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.data.model.DashboardCategoryInfo
import com.ecommerce.albeliapp.dashboard.data.model.DashboardResponse
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.DashboardCategoryAdapter
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.DashboardBannerPagerDotsAdapter
import com.ecommerce.albeliapp.dashboard.ui.activity.DashboardActivity
import com.ecommerce.albeliapp.dashboard.ui.activity.ProductFilterActivity
import com.ecommerce.albeliapp.dashboard.ui.activity.SearchProductActivity
import com.ecommerce.albeliapp.dashboard.ui.adapter.DashboardBannerPagerAdapter
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.FragmentHomeBinding
import com.ecommerce.utilities.callback.DialogButtonClickListener
import com.ecommerce.utilities.utils.AlertDialogHelper
import com.ecommerce.utilities.utils.NetworkHelper
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.parceler.Parcels
import java.util.Timer
import java.util.TimerTask

class HomeFragment : BaseFragment(), View.OnClickListener, SelectItemListener,
    DialogButtonClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mContext: Context
    private lateinit var adapterBannerDots: DashboardBannerPagerDotsAdapter
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var adapterCategoryProduct: DashboardCategoryAdapter? = null
    private lateinit var dashboardData: DashboardResponse
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    val handler = Handler(Looper.getMainLooper())
    var isShowingVersionUpdateDialog = false
    private var lastClickedTime: Long = 0

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        mContext = requireActivity()
        dashboardResponseObservers()
        initialDataLoad()
        binding.imgSearch.setOnClickListener(this)
        return binding.root
    }

    private fun loadDashboardApi(isProgress: Boolean) {
        if (isProgress)
            showCustomProgressDialog(binding.progressBarView.routProgress)
        dashboardViewModel.getDashboardResponse()
    }

    fun initialDataLoad() {
        val user: User? = AppUtils.getUserPreference(mContext)
        if (user != null) {
            binding.txtUserName.text = "Hi ${AppUtils.getUserPreference(mContext)?.first_name}"
        } else {
            binding.txtUserName.text = "Hi Guest"
        }
        if (!NetworkHelper.isConnected(requireContext())) {
            ToastHelper.showSnackBar(
                requireActivity(),
                requireActivity().getString(R.string.alert_no_connection),
                binding.root
            )
        } else {
            loadDashboardApi(true)
        }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime();
            when (v.id) {
                R.id.imgSearch -> {
                    moveActivity(mContext, SearchProductActivity::class.java, false, false, null)
                }
            }
        }
    }

//    fun moveToProductsList(productType: Int) {
//        val intent = Intent(mContext, FeaturedProductsActivity::class.java)
//        intent.putExtra(AppConstants.IntentKey.PRODUCT_TYPE, productType);
//        resultProductsLists.launch(intent)
//    }

    var resultProductsLists =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

            }
        }

    private fun setCategoryProductsAdapter(list: MutableList<DashboardCategoryInfo>?) {
        if (list != null && list.size > 0) {
            binding.rvCategories.visibility = View.VISIBLE
            binding.rvCategories.setHasFixedSize(true)
            adapterCategoryProduct =
                DashboardCategoryAdapter(mContext, list, this)
            binding.rvCategories.adapter = adapterCategoryProduct
//            val layoutManager =
//                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
//            binding.rvCategories.layoutManager = layoutManager
        } else {
            binding.rvCategories.visibility = View.GONE
        }
    }

    private fun setBannerAdapter(list: List<String>?) {
        if (!list.isNullOrEmpty()) {
            binding.pagerBannerSlider.visibility = View.VISIBLE
            binding.rvBannerDots.visibility = View.VISIBLE
            val adapterPager =
                DashboardBannerPagerAdapter(
                    mContext,
                    list,this
                )
            binding.pagerBannerSlider.adapter = adapterPager
            binding.pagerBannerSlider.addOnPageChangeListener(object :
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
            binding.rvBannerDots.layoutManager = linearLayoutManager
            adapterBannerDots =
                DashboardBannerPagerDotsAdapter(mContext, list!!.size)
            binding.rvBannerDots.adapter = adapterBannerDots

            startTimer()
        } else {
            binding.pagerBannerSlider.visibility = View.GONE
            binding.rvBannerDots.visibility = View.GONE

            stopTimer()
        }
    }

    private fun dashboardResponseObservers() {
        dashboardViewModel.dashboardResponse.observe(requireActivity()) { response ->
            hideProgressDialog()
            hideCustomProgressDialog(binding.progressBarView.routProgress)
            try {
                if (response == null) {
//                    AlertDialogHelper.showDialog(
//                        mContext, null,
//                        mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
//                        null, false, null, 0
//                    )
                    ToastHelper.showSnackBar(
                        mContext,
                        mContext.getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {
                        dashboardData = response
                        setCategoryProductsAdapter(response.categorySlider)
                        setBannerAdapter(response.mainSlider)
                        MyApplication().preferencePutString(
                            AppConstants.SharedPrefKey.DEFAULT_CATEGORY_NAME,
                            response.categoryName
                        )
                        MyApplication().preferencePutInteger(
                            AppConstants.SharedPrefKey.DEFAULT_CATEGORY_ID,
                            response.categoryID
                        )
//                        updateAppDialog(response.version_code)
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }


    override fun onSelectItem(position: Int, action: Int, productType: Int) {
        when (action) {
            AppConstants.Action.CATEGORY_DETAILS -> {
                if (activity is DashboardActivity) (activity as DashboardActivity?)!!.refreshCategoryProducts(adapterCategoryProduct!!.list[position].category_id,adapterCategoryProduct!!.list[position].categoryName)
            }
        }
    }

    var resultProductDetailsActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null
                && result.resultCode == Activity.RESULT_OK
            ) {
//                loadDashboardApi(true)
//                if (activity is DashBoardActivity) (activity as DashBoardActivity?)!!.refreshCompareCount()
            }
        }


    private fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    if (binding.pagerBannerSlider.currentItem == dashboardData.mainSlider.size - 1) {
                        binding.pagerBannerSlider.currentItem = 0
                    } else {
                        binding.pagerBannerSlider.currentItem =
                            binding.pagerBannerSlider.currentItem + 1
                    }
                }
            }
        }
    }

//    fun updateAppDialog(versionCode: Int) {
//        if (BuildConfig.VERSION_CODE < versionCode) {
//            isShowingVersionUpdateDialog = true
//            AlertDialogHelper.showDialog(
//                mContext, getString(R.string.title_update_app),
//                getString(R.string.msg_update_app), mContext.getString(R.string.ok),
//                null, false, this, AppConstants.DialogIdentifier.UPDATE_APP
//            )
//        } else {
//            isShowingVersionUpdateDialog = false
//        }
//    }

    override fun onPositiveButtonClicked(dialogIdentifier: Int) {
        if (dialogIdentifier == AppConstants.DialogIdentifier.UPDATE_APP) {
            isShowingVersionUpdateDialog = false
            AppUtils.openPlayStore(mContext)
        }
    }

    override fun onNegativeButtonClicked(dialogIdentifier: Int) {

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