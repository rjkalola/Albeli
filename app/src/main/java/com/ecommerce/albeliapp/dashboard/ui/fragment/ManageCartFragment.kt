package com.ecommerce.albeliapp.dashboard.data.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.callback.SelectItemListener
import com.ecommerce.albeliapp.common.ui.fragment.BaseFragment
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductInfo
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.CartListAdapter
import com.ecommerce.albeliapp.dashboard.ui.activity.ProductDetailsActivity
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.FragmentCartBinding
import com.ecommerce.utilities.callback.DialogButtonClickListener
import com.ecommerce.utilities.utils.AlertDialogHelper
import com.ecommerce.utilities.utils.NetworkHelper
import com.ecommerce.utilities.utils.StringHelper
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.String
import java.util.Locale


class ManageCartFragment : BaseFragment(), View.OnClickListener, SelectItemListener,
    DialogButtonClickListener {
    private lateinit var binding: FragmentCartBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    var adapter: CartListAdapter? = null
    var categoryProductsResponse: CategoryProductsResponse? = null
    private var lastClickedTime: Long = 0
    private var selectedPosition: Int = 0
    private var totalItems: Int = 0
    private var payingAmount = 0.0f
    private var discountAmt = 0.0f
    private var productInfo = ""
    private var isUpdate = false
    private var discountPerc = 0.0f

    companion object {
        fun newInstance(): ManageCartFragment {
            return ManageCartFragment()
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        mContext = requireActivity()
        categoryProductsListObservers()
        addRemoveQtyObservers()
        mCouponCodeResponseObservers()
        loadData(true)
        binding.tvApplyCoupon.setOnClickListener(this)
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
                showCustomProgressDialog(binding.progressBarView.routProgress)
            dashboardViewModel.getCartListResponse()
        }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime()
            when (v.id) {
                R.id.tvApplyCoupon -> {
                    if (checkCouponValidation(binding.edtCoupon.text.toString().trim())) {
                        showProgressDialog(mContext, "")
                        dashboardViewModel.verifyCouponCode(
                            binding.edtCoupon.text.toString().trim()
                        )
                    }
                }
            }
        }

    }

    private fun checkCouponValidation(str: kotlin.String): Boolean {
        if (!TextUtils.isEmpty(str)) {
            return true
        }
        ToastHelper.showSnackBar(mContext, "Please enter valid voucher code.", binding.root)
        return false
    }


    public fun validate(): Boolean {
        return categoryProductsResponse?.Data!!.isNotEmpty()
    }

    private fun setAdapter(list: MutableList<CategoryProductInfo>?) {
        if (list != null && list.size > 0) {
            refreshCartPrices()
            binding.llCart.visibility = View.VISIBLE
            binding.tvEmptyView.visibility = View.GONE
            adapter = CartListAdapter(mContext, list, this)
            binding.rvCart.adapter = adapter
            val linearLayoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            binding.rvCart.layoutManager = linearLayoutManager
        } else {
            binding.llCart.visibility = View.GONE
            binding.tvEmptyView.visibility = View.VISIBLE
        }
    }


    private fun addRemoveQtyObservers() {
        dashboardViewModel.addProductToCartResponse.observe(requireActivity()) { response ->
            try {
                if (response == null) {
                    ToastHelper.showSnackBar(
                        mContext,
                        mContext.getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {
                        isUpdate = true
                        loadData(false)
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun mCouponCodeResponseObservers() {
        dashboardViewModel.mCouponCodeResponse.observe(requireActivity()) { response ->
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
                        if (!StringHelper.isEmpty(response.value)) {
                            ToastHelper.showSnackBar(
                                mContext,
                                response.Message!!,
                                binding.root
                            )
                            discountPerc = response.value.toFloat();
                            Log.e("test", "discountPerc:" + discountPerc)
                            refreshCartPrices();
                            binding.edtCoupon.setText("")
                        }
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
            hideCustomProgressDialog(binding.progressBarView.routProgress)
            hideProgressDialog()
            binding.progressBarCart.visibility = View.GONE
            try {
                if (response == null) {
                    ToastHelper.showSnackBar(
                        mContext,
                        mContext.getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {
                        categoryProductsResponse = response
                        setAdapter(response.Data)
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun refreshCartPrices() {
        var totalPrice = 0.0f
        var totalQty = 0

        for (i in categoryProductsResponse?.Data!!.indices) {
            totalQty += categoryProductsResponse?.Data!![i].qty
            totalPrice += categoryProductsResponse?.Data!![i].line_total
        }

        this.discountAmt = discountPerc * totalPrice / 100.0f
        Log.e("test", "discountAmt:" + discountAmt)
        payingAmount = totalPrice
        totalItems = totalQty

        val spannableStringBuilder = SpannableStringBuilder()
        binding.tvTotalPriceTitle.text = "Total Price ($totalQty item)"
        spannableStringBuilder.append(binding.tvTotalPriceTitle.text)
        spannableStringBuilder.append(":")
        spannableStringBuilder.append("\n")
        binding.tvTotalPrice.text = "₹" + String.format(
            Locale.getDefault(),
            "%.02f",
            payingAmount
        )
        if (discountAmt > 1.0) {
            binding.routNetPriceView.visibility = View.VISIBLE
            payingAmount -= discountAmt
            binding.tvNetPrice.text = "₹" + String.format(
                Locale.getDefault(),
                "%.02f",
                payingAmount
            )
            spannableStringBuilder.append(binding.tvNetPrice.text)
        } else {
            binding.routNetPriceView.visibility = View.GONE
            spannableStringBuilder.append(binding.tvTotalPrice.text)
        }
        if (discountAmt != 0.0f) {
            binding.tvDiscount.text = "₹" + String.format(
                Locale.getDefault(),
                "%.02f",
                discountAmt
            )
        } else {
            binding.tvDiscount.text = "-"
        }
        productInfo = spannableStringBuilder.toString()
        Log.e("test", "productInfo:" + productInfo)
        Log.e("test", "payingAmount:" + payingAmount)
        Log.e("test", "totalItems:" + totalItems)
    }

    override fun onSelectItem(position: Int, action: Int, productType: Int) {
        when (action) {
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

            AppConstants.Action.INCREASE_QTY -> {
                if (adapter!!.list[position].qty < adapter!!.list[position].product_qty) {
                    binding.progressBarCart.visibility = View.VISIBLE
                    dashboardViewModel.updateProductToCartResponse(
                        adapter!!.list[position].id,
                        adapter!!.list[position].qty + 1
                    )
                }
            }

            AppConstants.Action.DECREASE_QTY -> {
                if (adapter!!.list[position].qty > 2) {
                    binding.progressBarCart.visibility = View.VISIBLE
                    dashboardViewModel.updateProductToCartResponse(
                        adapter!!.list[position].id,
                        adapter!!.list[position].qty - 1
                    )
                }
            }

            AppConstants.Action.REMOVE_CART_ITEM -> {
                selectedPosition = position
                AlertDialogHelper.showDialog(
                    mContext,
                    "Albeli",
                    "Are you sure want to remove item from the Cart?",
                    mContext.getString(R.string.ok),
                    getString(R.string.cancel),
                    false,
                    this,
                    AppConstants.DialogIdentifier.REMOVE_CART_ITEM
                )
            }
        }
    }

    override fun onPositiveButtonClicked(dialogIdentifier: Int) {
        if (dialogIdentifier == AppConstants.DialogIdentifier.REMOVE_CART_ITEM) {
            binding.progressBarCart.visibility = View.VISIBLE
            dashboardViewModel.removeProductFromCartResponse(
                adapter!!.list[selectedPosition].id
            )
        }
    }

    override fun onNegativeButtonClicked(dialogIdentifier: Int) {

    }

    var resultProductDetailsActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null
                && result.resultCode == Activity.RESULT_OK
            ) {

            }
        }

    fun isUpdate(): Boolean {
        return isUpdate
    }

    fun getTotalItems(): Int {
        return totalItems
    }

    fun getTotalAmount(): kotlin.String {
        return payingAmount.toString()
    }

    fun getProductInfo(): kotlin.String {
        return productInfo
    }

}