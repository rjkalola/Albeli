package com.ecommerce.albeliapp.dashboard.data.ui.fragment

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
import com.ecommerce.albeliapp.MyApplication
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.api.model.ModuleInfo
import com.ecommerce.albeliapp.common.callback.SelectItemListener
import com.ecommerce.albeliapp.common.ui.fragment.BaseFragment
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductInfo
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryResponse
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.CategoryProductsAdapter
import com.ecommerce.albeliapp.dashboard.ui.activity.ProductDetailsActivity
import com.ecommerce.albeliapp.dashboard.ui.activity.ProductFilterActivity
import com.ecommerce.albeliapp.dashboard.ui.activity.SearchProductActivity
import com.ecommerce.albeliapp.dashboard.ui.fragment.SelectItemBottomSheetDialog
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.FragmentCategoryProductsBinding
import com.ecommerce.albeliapp.databinding.FragmentWishlistProductsBinding
import com.ecommerce.utilities.utils.NetworkHelper
import com.ecommerce.utilities.utils.StringHelper
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.parceler.Parcels

class WishlistProductsFragment : BaseFragment(), View.OnClickListener, SelectItemListener {
    private lateinit var binding: FragmentWishlistProductsBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    var adapter: CategoryProductsAdapter? = null
    var visibleItemCount = 0
    var totalItemCount = 0
    var pastVisibleItems = 0
    var offset = 0
    var loading = true
    var mIsLastPage = false
    var categoryProductsResponse: CategoryProductsResponse? = null
    private var lastClickedTime: Long = 0

    companion object {
        fun newInstance(): WishlistProductsFragment {
            return WishlistProductsFragment()
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_wishlist_products, container, false)
        mContext = requireActivity()
        categoryProductsListObservers()
        baseResponseObservers()
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData(false, true)
        }

        return binding.root
    }

    fun loadData(isProgress: Boolean, isClear: Boolean) {
        if (!NetworkHelper.isConnected(requireContext())) {
            ToastHelper.showSnackBar(
                requireActivity(),
                mContext.getString(R.string.alert_no_connection),
                binding.root
            )
        } else {
            if (isProgress)
                showCustomProgressDialog(binding.progressBarView.routProgress)
            dashboardViewModel.getWishlistProductsResponse(
                AppConstants.DataLimit.PRODUCTS_LIMIT,
                offset,
            )
        }
    }

    fun initialDataLoad() {
        loadData(true, true)
    }

    fun loadDataFromDashBoard() {
        initialDataLoad()
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime();
            when (v.id) {

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
                    dashboardViewModel.removeProductToWatchListResponse(adapter!!.list[position].id.toString())
                    adapter!!.list.removeAt(position)
                    adapter!!.notifyDataSetChanged()
                    if(adapter!!.list.size == 0){
                        binding.rvCategoryItems.visibility = View.GONE
                        binding.txtEmptyPlaceHolder.visibility = View.VISIBLE
                    }
                } else {
                    AppUtils.showLoginRequiredAlertDialog(mContext)
                }
            }

            AppConstants.Action.PRODUCTS_DETAILS -> {
                val intent = Intent(mContext, ProductDetailsActivity::class.java)
                val bundle = Bundle()
                bundle.putString(
                    AppConstants.IntentKey.PRODUCT_ID,
                    adapter!!.list[position].id.toString()
                )
                intent.putExtras(bundle)
                resultProductDetailsActivity.launch(intent)
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