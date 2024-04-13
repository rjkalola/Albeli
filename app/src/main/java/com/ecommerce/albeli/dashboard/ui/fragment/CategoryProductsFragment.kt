package com.ecommerce.albeli.dashboard.data.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeli.MyApplication
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.api.model.ModuleInfo
import com.ecommerce.albeli.common.callback.SelectItemListener
import com.ecommerce.albeli.common.ui.fragment.BaseFragment
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.common.utils.AppUtils
import com.ecommerce.albeli.dashboard.data.model.CategoryProductInfo
import com.ecommerce.albeli.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeli.dashboard.data.model.CategoryResponse
import com.ecommerce.albeli.dashboard.data.ui.adapter.CategoryProductsAdapter
import com.ecommerce.albeli.dashboard.ui.activity.ProductDetailsActivity
import com.ecommerce.albeli.dashboard.ui.activity.ProductFilterActivity
import com.ecommerce.albeli.dashboard.ui.activity.SearchProductActivity
import com.ecommerce.albeli.dashboard.ui.fragment.SelectItemBottomSheetDialog
import com.ecommerce.albeli.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeli.databinding.FragmentCategoryProductsBinding
import com.ecommerce.utilities.utils.NetworkHelper
import com.ecommerce.utilities.utils.StringHelper
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.parceler.Parcels

class CategoryProductsFragment : BaseFragment(), View.OnClickListener, SelectItemListener {
    private lateinit var binding: FragmentCategoryProductsBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    var adapter: CategoryProductsAdapter? = null
    var visibleItemCount = 0
    var totalItemCount = 0
    var pastVisibleItems = 0
    var offset = 0
    var loading = true
    var mIsLastPage = false
    var sort = 0;
    var categoryId = 0
    var minPrice = 0
    var maxPrice = 15000
    var categoryName = ""
    var categoryResponse: CategoryResponse? = null
    var categoryProductsResponse: CategoryProductsResponse? = null
    private var lastClickedTime: Long = 0
    private lateinit var selectItemBottomSheetDialog: SelectItemBottomSheetDialog

    companion object {
        fun newInstance(): CategoryProductsFragment {
            return CategoryProductsFragment()
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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_products, container, false)
        mContext = requireActivity()
        categoryProductsListObservers()
        categoryListObservers()
        baseResponseObservers()
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData(false, true)
        }
        dashboardViewModel.getCategoryListResponse()
        binding.txtCategory.setOnClickListener(this)
        binding.imgFilter.setOnClickListener(this)
        binding.imgSearch.setOnClickListener(this)
        categoryName = MyApplication().preferenceGetString(
            AppConstants.SharedPrefKey.DEFAULT_CATEGORY_NAME,
            ""
        )
        categoryId =
            MyApplication().preferenceGetInteger(
                AppConstants.SharedPrefKey.DEFAULT_CATEGORY_ID,
                0
            )
        return binding.root
    }

    fun loadData(isProgress: Boolean, isClear: Boolean) {
        if (!NetworkHelper.isConnected(requireContext())) {
            ToastHelper.showSnackBar(
                mContext,
                mContext.getString(R.string.alert_no_connection),
                binding.root
            )
        } else {
            if (isProgress)
                showCustomProgressDialog(binding.progressBarView.routProgress)
            dashboardViewModel.getCategoryProductsResponse(
                categoryId.toString(),
                AppConstants.DataLimit.PRODUCTS_LIMIT,
                offset,
            )
        }
    }

    fun initialDataLoad() {
        binding.txtCategory.text = categoryName
        loadData(true, true)
    }

    fun loadDataFromDashBoard(categoryId: String, categoryName: String) {
        if (!StringHelper.isEmpty(categoryId))
            this.categoryId = categoryId.toInt()
        this.categoryName = categoryName
        initialDataLoad()
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime();
            when (v.id) {
                R.id.txtCategory -> {
                    if (categoryResponse?.Data!!.isNotEmpty()) {
                        showSelectItemDialog(
                            categoryResponse?.Data!!,
                            getString(R.string.title_choose_category),
                            AppConstants.Action.SELECT_CATEGORY
                        )
                    }
                }

                R.id.imgFilter -> {
                    val intent = Intent(mContext, ProductFilterActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable(
                        AppConstants.IntentKey.FILTER_DATA, Parcels.wrap<MutableList<ModuleInfo>>(
                            categoryProductsResponse?.filter!!
                        )
                    )
                    bundle.putInt(AppConstants.IntentKey.FILTER_MIN_PRICE, minPrice)
                    bundle.putInt(AppConstants.IntentKey.FILTER_MAX_PRICE, maxPrice)
                    intent.putExtras(bundle)
                    resultFilter.launch(intent)
                }

                R.id.imgSearch -> {
                    moveActivity(mContext, SearchProductActivity::class.java, false, false, null)
                }
            }
        }

    }

    var resultFilter =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null
                && result.resultCode == Activity.RESULT_OK
            ) {
                val intent = result.data
                if (intent != null && intent.extras != null) {
                    val listFilters: MutableList<ModuleInfo> = ArrayList()
                    listFilters.addAll(
                        Parcels.unwrap<MutableList<ModuleInfo>>(
                            intent.getParcelableExtra(
                                AppConstants.IntentKey.FILTER_DATA
                            )
                        )
                    )
                    minPrice = intent.getIntExtra(AppConstants.IntentKey.FILTER_MIN_PRICE, 0)
                    maxPrice = intent.getIntExtra(AppConstants.IntentKey.FILTER_MAX_PRICE, 0)
                    Log.e("test", "minPrice:$minPrice")
                    Log.e("test", "maxPrice:$maxPrice")
                    categoryProductsResponse?.filter!!.clear()
                    categoryProductsResponse?.filter!!.addAll(listFilters)
                    val filterIds = intent.getStringExtra(AppConstants.IntentKey.FILTER_IDS)
                    if (!NetworkHelper.isConnected(requireContext())) {
                        ToastHelper.showSnackBar(
                            mContext,
                            mContext.getString(R.string.alert_no_connection),
                            binding.root
                        )
                    } else {
                        showCustomProgressDialog(binding.progressBarView.routProgress)
                        dashboardViewModel.getCategoryProductsResponse(
                            categoryId.toString(),
                            AppConstants.DataLimit.PRODUCTS_LIMIT,
                            offset,
                            filterIds!!,
                            minPrice.toString(),
                            maxPrice.toString()
                        )
                    }
                }
            }
        }

    private fun setAdapter(list: MutableList<CategoryProductInfo>?) {
        if (list != null && list.size > 0) {
            binding.rvCategoryItems.visibility = View.VISIBLE
            binding.txtEmptyPlaceHolder.visibility = View.GONE
            adapter = CategoryProductsAdapter(mContext, list, this)
            binding.rvCategoryItems.adapter = adapter
            val gridManager =
                GridLayoutManager(requireActivity(), 2)
            binding.rvCategoryItems.layoutManager = gridManager
            recyclerViewScrollListener(gridManager)
        } else {
            binding.rvCategoryItems.visibility = View.GONE
            binding.txtEmptyPlaceHolder.visibility = View.VISIBLE
        }
    }

    private fun recyclerViewScrollListener(layoutManager: LinearLayoutManager) {
        binding.rvCategoryItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = recyclerView.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            if (!mIsLastPage) {
                                loading = false
                                binding.loadMore.visibility = View.VISIBLE
                                loadData(false, false)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun categoryListObservers() {
        dashboardViewModel.categoryResponse.observe(requireActivity()) { response ->
            try {
                if (response == null) {

                } else {
                    if (response.IsSuccess) {
                        categoryResponse = response
                    } else {
//                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun baseResponseObservers() {
        dashboardViewModel.baseResponse.observe(requireActivity()) { response ->
            try {
                if (response == null) {
                    ToastHelper.showSnackBar(
                        mContext,
                        mContext.getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {

                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun categoryProductsListObservers() {
        dashboardViewModel.categoryProductsResponse.observe(requireActivity()) { response ->
//            hideProgressDialog()
            hideCustomProgressDialog(binding.progressBarView.routProgress)
            binding.swipeRefreshLayout.isRefreshing = false
            binding.loadMore.visibility = View.GONE
            try {
                if (response == null) {
                    ToastHelper.showSnackBar(
                        mContext,
                        mContext.getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {
                        if (offset == 0) {
                            categoryProductsResponse = response
                            setAdapter(response.Data)
                        } else if (response.Data.isNotEmpty()) {
                            if (adapter != null) {
                                adapter!!.addData(response.Data)
                                loading = true
                            }
                        } else if (response.offset == 0) {
                            loading = true
                        }
                        offset = response.offset
                        mIsLastPage = offset == 0
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun showSelectItemDialog(
        list: MutableList<ModuleInfo>,
        title: String? = "",
        identifyId: Int? = 0,
    ) {
        selectItemBottomSheetDialog =
            SelectItemBottomSheetDialog.newInstance(mContext, list, title, identifyId, this)
        selectItemBottomSheetDialog.show()
    }

    override fun onSelectItem(position: Int, action: Int, productType: Int) {
        when (action) {
            AppConstants.Action.WATCHLIST -> {
                if (!NetworkHelper.isConnected(requireContext())) {
                    ToastHelper.showSnackBar(
                        mContext,
                        mContext.getString(R.string.alert_no_connection),
                        binding.root
                    )
                } else if (AppUtils.isLogin(mContext)) {
                    if (adapter!!.list[position].wishlisted) {
                        dashboardViewModel.removeProductToWatchListResponse(adapter!!.list[position].id.toString())
                    } else {
                        dashboardViewModel.addProductToWatchListResponse(adapter!!.list[position].id.toString())
                    }
                } else {
                    AppUtils.showLoginRequiredAlertDialog(mContext)
                }
            }

            AppConstants.Action.PRODUCTS_DETAILS -> {
                val intent = Intent(mContext, ProductDetailsActivity::class.java)
                val bundle = Bundle()
                bundle.putString(AppConstants.IntentKey.PRODUCT_ID,adapter!!.list[position].id.toString())
                intent.putExtras(bundle)
                resultProductDetailsActivity.launch(intent)
            }
            AppConstants.Action.SELECT_CATEGORY -> {
                selectItemBottomSheetDialog.dismiss()
                categoryId = categoryResponse?.Data!![position].id
                categoryName = categoryResponse?.Data!![position].name
                binding.txtCategory.text = categoryName
                loadData(true, true)
            }
        }
    }

    var resultProductDetailsActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null
                && result.resultCode == Activity.RESULT_OK
            ) {
                offset = 0
                loadData(true, true)
            }
        }

}