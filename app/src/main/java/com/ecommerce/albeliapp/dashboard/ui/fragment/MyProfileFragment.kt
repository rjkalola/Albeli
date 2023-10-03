package com.ecommerce.albeliapp.dashboard.data.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeliapp.MyApplication
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.ui.fragment.BaseFragment
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.ui.activity.AddAddressActivity
import com.ecommerce.albeliapp.dashboard.ui.activity.DashboardActivity
import com.ecommerce.albeliapp.dashboard.ui.activity.EditProfileActivity
import com.ecommerce.albeliapp.dashboard.ui.activity.NotificationListActivity
import com.ecommerce.albeliapp.dashboard.ui.activity.OrdersListActivity
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.FragmentMyProfileBinding
import com.ecommerce.utilities.callback.DialogButtonClickListener
import com.ecommerce.utilities.utils.AlertDialogHelper
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyProfileFragment : BaseFragment(), View.OnClickListener,
    DialogButtonClickListener {
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0

    companion object {
        fun newInstance(): MyProfileFragment {
            return MyProfileFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        mContext = requireActivity()
        mLogoutResponseObservers()
        binding.tvMyOrders.setOnClickListener(this)
        binding.tvMyWishlist.setOnClickListener(this)
        binding.tvMyNotificationList.setOnClickListener(this)
        binding.tvEditProfile.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime()
            when (v.id) {
                R.id.tvAdd -> {
                    val intent = Intent(mContext, AddAddressActivity::class.java)
                    resultAddAddressActivity.launch(intent)
                }

                R.id.tvEditProfile -> {
                    moveActivity(mContext, EditProfileActivity::class.java, false, false, null)
                }

                R.id.tvMyWishlist -> {
                    if (activity is DashboardActivity) (activity as DashboardActivity?)!!.setupTab(2)
                }

                R.id.tvMyNotificationList -> {
                    moveActivity(mContext, NotificationListActivity::class.java, false, false, null)
                }

                R.id.tvMyOrders -> {
                    moveActivity(mContext, OrdersListActivity::class.java, false, false, null)
                }

                R.id.btnLogout -> {
                    AlertDialogHelper.showDialog(
                        mContext,
                        "Albeli",
                        "Are you sure want to Logout?",
                        mContext.getString(R.string.yes),
                        getString(R.string.no),
                        false,
                        this,
                        AppConstants.DialogIdentifier.LOGOUT
                    )
                }
            }
        }

    }

    private fun mLogoutResponseObservers() {
        dashboardViewModel.mLogoutResponse.observe(requireActivity()) { response ->
            hideProgressDialog()
            try {
                if (response == null) {
                    ToastHelper.showSnackBar(
                        mContext,
                        mContext.getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {
                        MyApplication().clearData()
                        moveActivity(
                            mContext,
                            DashboardActivity::class.java, true, true, null
                        )
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    override fun onPositiveButtonClicked(dialogIdentifier: Int) {
        if (dialogIdentifier == AppConstants.DialogIdentifier.LOGOUT) {
            showProgressDialog(mContext, "")
            dashboardViewModel.mLogoutResponse()
        }
    }

    override fun onNegativeButtonClicked(dialogIdentifier: Int) {

    }


    var resultAddAddressActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null
                && result.resultCode == Activity.RESULT_OK
            ) {

            }
        }

}