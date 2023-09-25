package com.ecommerce.albeliapp.dashboard.ui.activity


import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.data.model.NotificationInfo
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.NotificationListAdapter
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.ActivityNotificationListBinding
import com.ecommerce.utilities.utils.NetworkHelper
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotificationListActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityNotificationListBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var pastVisibleItems = 0
    var offset = 0
    var loading = true
    var mIsLastPage = false
    var adapter: NotificationListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_list)
        setStatusBarColor()
        mContext = this
        mNotificationResponse()
        binding.imgBack.setOnClickListener(this)
        loadData(true)
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

    fun loadData(isProgress: Boolean) {
        if (!NetworkHelper.isConnected(mContext)) {
            ToastHelper.showSnackBar(
                mContext,
                mContext.getString(R.string.alert_no_connection),
                binding.root
            )
        } else {
            if (isProgress)
                showCustomProgressDialog(binding.progressBarView.routProgress)
            dashboardViewModel.getNotificationList(
                AppConstants.DataLimit.PRODUCTS_LIMIT,
                offset,
            )
        }
    }

    private fun setAdapter(list: MutableList<NotificationInfo>?) {
        if (list != null && list.size > 0) {
            binding.rvNotificationList.visibility = View.VISIBLE
            binding.txtEmptyPlaceHolder.visibility = View.GONE
            adapter = NotificationListAdapter(mContext, list)
            binding.rvNotificationList.adapter = adapter
            val layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            binding.rvNotificationList.layoutManager = layoutManager
            recyclerViewScrollListener(layoutManager)
        } else {
            binding.rvNotificationList.visibility = View.GONE
            binding.txtEmptyPlaceHolder.visibility = View.VISIBLE
        }
    }

    private fun recyclerViewScrollListener(layoutManager: LinearLayoutManager) {
        binding.rvNotificationList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                                loadData(false)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun mNotificationResponse() {
        dashboardViewModel.mNotificationResponse.observe(this) { response ->
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

}