package com.ecommerce.albeliapp.dashboard.ui.activity


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.api.model.ModuleInfo
import com.ecommerce.albeliapp.common.callback.SelectItemListener
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductInfo
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.CategoryProductsAdapter
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.ActivitySearchProductsBinding
import com.ecommerce.utilities.utils.NetworkHelper
import com.ecommerce.utilities.utils.ToastHelper
import com.ecommerce.utilities.utils.ValidationUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.parceler.Parcels


class SearchProductActivity : BaseActivity(), SelectItemListener, View.OnClickListener {
    private lateinit var binding: ActivitySearchProductsBinding
    private lateinit var mContext: Context;
    var adapter: CategoryProductsAdapter? = null
    private val dashboardViewModel: DashboardViewModel by viewModel()
    var visibleItemCount = 0
    var totalItemCount = 0
    var pastVisibleItems = 0
    var offset = 0
    var loading = true
    var mIsLastPage = false
    private var lastClickedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_products)
        setStatusBarColor()
        mContext = this
        categoryProductsListObservers()
        baseResponseObservers()
        binding.edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId === EditorInfo.IME_ACTION_SEARCH) {
                if (valid()) {
                    hideKeyBoard()
                    loadData(true, binding.edtSearch.text.toString())
                }
                return@setOnEditorActionListener true
            }
            false
        }
        binding.routBack.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
    }

    fun loadData(isProgress: Boolean, search: String) {
        if (!NetworkHelper.isConnected(mContext)) {
            ToastHelper.showSnackBar(
                mContext,
                mContext.getString(R.string.alert_no_connection),
                binding.root
            )
        } else {
            if (isProgress)
                showProgressDialog(mContext, "")
            dashboardViewModel.searchProductsResponse(
                search,
                AppConstants.DataLimit.PRODUCTS_LIMIT,
                offset,
            )
        }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime();
            when (v.id) {
                R.id.routBack -> {
                    onBackPressed()
                }
                R.id.ivClose -> {
                    binding.edtSearch.setText("")
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
                GridLayoutManager(mContext, 2)
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
                                loadData(false, binding.edtSearch.text.toString())
                            }
                        }
                    }
                }
            }
        })
    }

    private fun valid(): Boolean {
        var valid = true
        if (ValidationUtil.isEmptyEditText(binding.edtSearch.text.toString().trim())) {
            valid = false
        }
        return valid
    }

    private fun categoryProductsListObservers() {
        dashboardViewModel.categoryProductsResponse.observe(this) { response ->
            hideProgressDialog()
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

    private fun baseResponseObservers() {
        dashboardViewModel.baseResponse.observe(this) { response ->
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

    override fun onSelectItem(position: Int, action: Int, productType: Int) {
        when (action) {
            AppConstants.Action.WATCHLIST -> {
                if (!NetworkHelper.isConnected(this)) {
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

//            AppConstants.Action.DIRECTORY_DETAILS -> {
////                showSellerDetailsDialog()
//                val intent = Intent(mContext, SellerDetailsActivity::class.java)
//                val bundle = Bundle()
//                bundle.putParcelable(
//                    AppConstants.IntentKey.DIRECTORY_INFO, Parcels.wrap<DirectoryInfo?>(
//                        adapter!!.list[position]
//                    )
//                )
//                intent.putExtras(bundle)
//                resultSellerDetailsActivity.launch(intent)
//            }
        }
    }

    fun showKeyBoard() {
        val imm =
            mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyBoard() {
        val imm =
            mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.routRootView.windowToken, 0)
    }
}