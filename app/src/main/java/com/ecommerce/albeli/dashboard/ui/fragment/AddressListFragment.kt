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
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.callback.SelectItemListener
import com.ecommerce.albeli.common.ui.fragment.BaseFragment
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.common.utils.AppUtils
import com.ecommerce.albeli.dashboard.data.model.AddressInfo
import com.ecommerce.albeli.dashboard.data.model.AddressResponse
import com.ecommerce.albeli.dashboard.data.ui.adapter.AddressListAdapter
import com.ecommerce.albeli.dashboard.ui.activity.AddAddressActivity
import com.ecommerce.albeli.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeli.databinding.FragmentAddressBinding
import com.ecommerce.utilities.callback.DialogButtonClickListener
import com.ecommerce.utilities.utils.NetworkHelper
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.parceler.Parcels

class AddressListFragment : BaseFragment(), View.OnClickListener, SelectItemListener,
    DialogButtonClickListener {
    private lateinit var binding: FragmentAddressBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    var adapter: AddressListAdapter? = null
    var addressResponse: AddressResponse? = null
    private var lastClickedTime: Long = 0
    private var selectedPosition: Int = 0
    private var addressId = 0

    companion object {
        fun newInstance(): AddressListFragment {
            return AddressListFragment()
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false)
        mContext = requireActivity()
        addressListObservers()
        makeDefaultAddressObservers()
        binding.tvAdd.setOnClickListener(this)
        return binding.root
    }

    fun loadData(isProgress: Boolean) {
        if (!NetworkHelper.isConnected(requireContext())) {
            ToastHelper.showSnackBar(
                mContext,
                mContext.getString(R.string.alert_no_connection),
                binding.root
            )
        } else {
            if (isProgress)
                showProgressDialog(mContext, "")
            dashboardViewModel.getAddressListResponse()
        }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime()
            when (v.id) {
                R.id.tvAdd -> {
                    val intent = Intent(mContext, AddAddressActivity::class.java)
                    resultAddAddressActivity.launch(intent)
                }
            }
        }

    }

    public fun validate(): Boolean {
        return addressResponse?.Data!!.isNotEmpty()
    }

    public fun isDefaultAddress(): Boolean {
        var isDefaultAddress = false
        for (i in addressResponse?.Data!!.indices){
            if(addressResponse?.Data!![i].is_default == "1"){
                isDefaultAddress = true
                addressId = addressResponse?.Data!![i].id
            }
        }
        return isDefaultAddress
    }

    private fun setAdapter(list: MutableList<AddressInfo>?) {
        if (list != null && list.size > 0) {
            binding.rvList.visibility = View.VISIBLE
            adapter = AddressListAdapter(mContext, list, this)
            binding.rvList.adapter = adapter
        } else {
            binding.rvList.visibility = View.GONE
        }
    }


    private fun makeDefaultAddressObservers() {
        dashboardViewModel.makeDefaultAddress.observe(requireActivity()) { response ->
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
                        loadData(false)
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun addressListObservers() {
        dashboardViewModel.addressResponse.observe(requireActivity()) { response ->
            hideCustomProgressDialog(binding.progressBarView.routProgress)
            hideProgressDialog()
            binding.progressBarAddress.visibility = View.GONE
            try {
                if (response == null) {
                    ToastHelper.showSnackBar(
                        mContext,
                        mContext.getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {
                        addressResponse = response
                        setAdapter(response.Data)
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
            AppConstants.Action.ADDRESS_DETAILS -> {
                val intent = Intent(mContext, AddAddressActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(
                    AppConstants.IntentKey.ADDRESS_INFO, Parcels.wrap<AddressInfo?>(
                        adapter!!.list[position]
                    )
                )
                intent.putExtras(bundle)
                resultAddAddressActivity.launch(intent)
            }

            AppConstants.Action.CHANGE_DEFAULT_ADDRESS -> {
                Log.e("test","CHANGE_DEFAULT_ADDRESS")
                showProgressDialog(mContext,"")
                dashboardViewModel.makeDefaultAddress(adapter!!.list[position].id)
            }
        }
    }

    override fun onPositiveButtonClicked(dialogIdentifier: Int) {
        if (dialogIdentifier == AppConstants.DialogIdentifier.REMOVE_CART_ITEM) {
//            binding.progressBarCart.visibility = View.VISIBLE
//            dashboardViewModel.removeProductFromCartResponse(
//                adapter!!.list[selectedPosition].id
//            )
        }
    }

    override fun onNegativeButtonClicked(dialogIdentifier: Int) {

    }

    fun getAddressId(): Int {
        return addressId
    }


    var resultAddAddressActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null
                && result.resultCode == Activity.RESULT_OK
            ) {
                loadData(true)
            }
        }

}