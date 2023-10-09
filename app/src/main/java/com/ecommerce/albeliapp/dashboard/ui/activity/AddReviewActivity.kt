package com.ecommerce.albeliapp.dashboard.ui.activity


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.RatingBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import com.ecommerce.albeliapp.databinding.ActivityAddReviewBinding
import com.ecommerce.utilities.utils.ToastHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddReviewActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var mContext: Context
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var lastClickedTime: Long = 0
    private var productId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_review)
        setStatusBarColor()
        mContext = this
        storeReviewResponse()
        binding.imgBack.setOnClickListener(this)
        binding.btnSubmitReview.setOnClickListener(this)
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, _ ->
            if (rating < 1.0f) ratingBar.rating = 1.0f
        }
        getIntentData()
    }

    private fun getIntentData() {
        if (intent.extras != null && intent.hasExtra(AppConstants.IntentKey.PRODUCT_ID)) {
            productId = intent.getIntExtra(AppConstants.IntentKey.PRODUCT_ID, 0)
        }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime()
            when (v.id) {
                R.id.imgBack ->
                    onBackPressed()

                R.id.btnSubmitReview -> {
                    val reviewText: String = binding.edtReview.text.toString().trim()
                    val user = AppUtils.getUserPreference(mContext)
                    val name = user?.first_name + " " + user?.last_name
                    val rating = binding.ratingBar.rating.toInt()
                    Log.e("test","productId:"+productId)
                    Log.e("test","rating:"+rating)
                    Log.e("test","name:"+name)
                    Log.e("test","reviewText:"+reviewText)
                    if (checkValidation(reviewText)) {
                        showProgressDialog(mContext, "")
                        dashboardViewModel.storeReview(
                            productId,
                            rating,
                            name,
                            reviewText
                        )
                    }
                }

            }
        }
    }

    private fun checkValidation(str: String): Boolean {
        if (!TextUtils.isEmpty(str)) {
            return true
        }
        ToastHelper.showSnackBar(mContext, "Please enter some comment", binding.routBack)
        return false
    }

    private fun storeReviewResponse() {
        dashboardViewModel.mStoreReviewResponse.observe(this) { response ->
            hideProgressDialog()
            try {
                if (response == null) {
                    ToastHelper.showSnackBar(
                        mContext,
                        getString(R.string.error_unknown),
                        binding.root
                    )
                } else {
                    if (response.IsSuccess) {
                        ToastHelper.showSnackBar(mContext, response.Message!!, binding.routBack)
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        AppUtils.handleUnauthorized(mContext, response, binding.root)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

}