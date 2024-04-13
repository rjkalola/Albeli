package com.ecommerce.albeli.dashboard.data.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.ui.fragment.BaseFragment
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.common.utils.AppUtils
import com.ecommerce.albeli.dashboard.ui.activity.DashboardActivity
import com.ecommerce.albeli.dashboard.ui.activity.ManageCartActivity
import com.ecommerce.albeli.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeli.databinding.FragmentPaymentBinding
import com.ecommerce.utilities.callback.DialogButtonClickListener
import com.ecommerce.utilities.utils.AlertDialogHelper
import com.ecommerce.utilities.utils.ToastHelper
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel


class PaymentFragment : BaseFragment(), View.OnClickListener,
    DialogButtonClickListener,
    PaymentResultListener {
    private lateinit var binding: FragmentPaymentBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0
    private var addressId: Int = 0
    private var totalItems: Int = 0
    private var totalAmount: String = ""

    companion object {
        fun newInstance(): PaymentFragment {
            return PaymentFragment()
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        mContext = requireActivity()
        mPlaceOrderResponseObservers()
        binding.tvAddress.setOnClickListener(this)
        binding.tvPhone.setOnClickListener(this)
        binding.rgOptions.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbOnline) {
                binding.llInfo.visibility = View.GONE
                if (activity is ManageCartActivity) (activity as ManageCartActivity?)!!.setContinueButtonText(
                    getString(R.string.make_payment)
                )
            } else if (checkedId == R.id.rbCOD) {
                binding.llInfo.visibility = View.VISIBLE
                if (activity is ManageCartActivity) (activity as ManageCartActivity?)!!.setContinueButtonText(
                    "Pick-up from Store"
                )
            }
        }
        Checkout.preload(mContext)
        return binding.root
    }

    fun initialLoad(addressId: Int, totalItems: Int, totalAmount: String, productInfo: String) {
        this.addressId = addressId
        this.totalItems = totalItems
        this.totalAmount = totalAmount
        binding.tvDesc.text = productInfo
        binding.rgOptions.check(R.id.rbOnline)
        binding.llInfo.visibility = View.GONE
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime()
            when (v.id) {
                R.id.tvAddress -> {
                    context!!.startActivity(
                        Intent(
                            "android.intent.action.VIEW",
                            Uri.parse("https://maps.app.goo.gl/Hu935iwyGKw2cbon9")
                        )
                    )
                }

                R.id.tvPhone -> {
                    val intent = Intent("android.intent.action.DIAL")
                    intent.data = Uri.parse("tel:" + binding.tvPhone.text.toString())
                    context!!.startActivity(intent)
                }
            }
        }

    }

    fun onclickPayment() {
        AlertDialogHelper.showDialog(
            mContext,
            "",
            getString(R.string.msg_submit_order),
            getString(R.string.yes),
            getString(R.string.no),
            false,
            this,
            AppConstants.DialogIdentifier.SUBMIT_ORDER
        )
    }

    private fun startPayment() {
        Log.e("test", "startPayment")
        val activity: Activity = requireActivity()

        val co = Checkout()
        co.setKeyID(getString(R.string.razorKey))
        try {
            val totalPayment = totalAmount.toFloat()
            val options = JSONObject()
            options.put("name", getString(R.string.app_name))
            options.put("description", "Payment")
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            Log.e("test", "totalPayment:" + totalPayment)
            Log.e("test", "totalAmount:" + totalAmount)
            options.put("amount", totalPayment * 100f)
//            options.put("send_sms_hash", true);

            val prefill = JSONObject()
            prefill.put("email", AppUtils.getUserPreference(mContext)?.email)
            prefill.put("contact", AppUtils.getUserPreference(mContext)?.phone)

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            ToastHelper.showSnackBar(
                mContext,
                "Error in payment: " + e.message,
                binding.root
            )
            e.printStackTrace()
        }
    }

    /*    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
            Log.e("test", "Payment Successful : Payment ID: $p0\nPayment Data: ${p1?.data}")
    //        try {
    //            alertDialogBuilder.setMessage("Payment Successful : Payment ID: $p0\nPayment Data: ${p1?.data}")
    //            alertDialogBuilder.show()
    //        } catch (e: Exception) {
    //            e.printStackTrace()
    //        }
        }

        override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
            Log.e("test", "Payment Failed : Payment Data: ${p2?.data}")
    //        try {
    //            alertDialogBuilder.setMessage("Payment Failed : Payment Data: ${p2?.data}")
    //            alertDialogBuilder.show()
    //        } catch (e: Exception) {
    //            e.printStackTrace()
    //        }
        }

        override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
            Log.e("test", "External wallet was selected : Payment Data: ${p1?.data}")
    //        try {
    //            alertDialogBuilder.setMessage("External wallet was selected : Payment Data: ${p1?.data}")
    //            alertDialogBuilder.show()
    //        } catch (e: Exception) {
    //            e.printStackTrace()
    //        }
        }*/

    override fun onPaymentSuccess(p0: String?) {
        Log.e("test", "Payment Successful : Payment ID: $p0")
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.e("test", "Payment Failed : Payment Data: ${p1}")
    }

    private fun mPlaceOrderResponseObservers() {
        dashboardViewModel.mPlaceOrderResponse.observe(requireActivity()) { response ->
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

    fun callOrderAPI(paymentType: Int, paymentCode: String) {
        showProgressDialog(mContext, "")
        dashboardViewModel.placeOrderResponse(addressId, paymentType, paymentCode)
    }

    override fun onPositiveButtonClicked(dialogIdentifier: Int) {
        if (dialogIdentifier == AppConstants.DialogIdentifier.SUBMIT_ORDER) {
            if (binding.rgOptions.checkedRadioButtonId == R.id.rbOnline) {
                if (activity is ManageCartActivity) (activity as ManageCartActivity?)!!.startPayment(
                    totalAmount
                )
            } else if (binding.rgOptions.checkedRadioButtonId == R.id.rbCOD) {
                callOrderAPI(2, "")
            }
        }
    }

    override fun onNegativeButtonClicked(dialogIdentifier: Int) {

    }

}