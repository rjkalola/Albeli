package com.ecommerce.albeliapp.dashboard.ui.activity


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.api.model.ModuleInfo
import com.ecommerce.albeliapp.common.ui.activity.BaseActivity
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.dashboard.callback.SelectSubItemListener
import com.ecommerce.albeliapp.dashboard.data.ui.adapter.FilterItemAdapter
import com.ecommerce.albeliapp.databinding.ActivityProductFilterBinding
import com.ecommerce.utilities.utils.ToastHelper
import org.parceler.Parcels


class ProductFilterActivity : BaseActivity(), View.OnClickListener, SelectSubItemListener {
    private lateinit var binding: ActivityProductFilterBinding
    private lateinit var mContext: Context;
    private var listFilters: MutableList<ModuleInfo> = ArrayList()
    private var lastClickedTime: Long = 0
    private var adapter: FilterItemAdapter? = null
    private var minPrice = 0
    private var maxPrice = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_filter)
        setStatusBarColor()
        mContext = this
        binding.routBack.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.txtReset.setOnClickListener(this)

        binding.priceRange.setOnRangeSeekbarChangeListener { number1, number2 ->
            if (binding.priceRange.isPressed) {
                minPrice = number1.toInt()
                maxPrice = number2.toInt()
                Log.e("test", "minPrice222:" + minPrice.toFloat())
                Log.e("test", "maxPrice222:" + maxPrice.toFloat())
                setRangeTextView(minPrice, maxPrice)
            }

        }
        getIntentData()
    }

    private fun getIntentData() {
        if (intent!!.hasExtra(AppConstants.IntentKey.FILTER_DATA)
            && Parcels.unwrap<MutableList<ModuleInfo>>(intent.getParcelableExtra(AppConstants.IntentKey.FILTER_DATA)) != null
        ) {
            listFilters.addAll(
                Parcels.unwrap<MutableList<ModuleInfo>>(
                    intent.getParcelableExtra(
                        AppConstants.IntentKey.FILTER_DATA
                    )
                )
            )
            setFilterAdapter(listFilters)
            minPrice = intent.getIntExtra(AppConstants.IntentKey.FILTER_MIN_PRICE, 0)
            maxPrice = intent.getIntExtra(AppConstants.IntentKey.FILTER_MAX_PRICE, 0)
            setRangeTextView(minPrice, maxPrice)
            binding.priceRange.setMinStartValue(minPrice.toFloat())
                .setMaxStartValue(maxPrice.toFloat())
            binding.priceRange.apply()
        }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - this.lastClickedTime >= 1000) {
            this.lastClickedTime = SystemClock.elapsedRealtime();
            when (v.id) {
                R.id.routBack, R.id.btnCancel -> {
                    onBackPressed()
                }

                R.id.btnApply -> {

                    var filterString = ""
                    val listIds: MutableList<Int> = ArrayList()
                    for (i in 0 until listFilters.size) {
                        for (j in 0 until listFilters[i].values.size) {
                            if(listFilters[i].values[j].check)
                                listIds.add(listFilters[i].values[j].id)
                        }
                    }
                    if(listIds.isNotEmpty())
                        filterString = TextUtils.join(",", listIds)

                    val bundle = Bundle()
                    bundle.putParcelable(
                        AppConstants.IntentKey.FILTER_DATA, Parcels.wrap<MutableList<ModuleInfo>>(
                            listFilters
                        )
                    )
                    bundle.putInt(AppConstants.IntentKey.FILTER_MIN_PRICE, minPrice)
                    bundle.putInt(AppConstants.IntentKey.FILTER_MAX_PRICE, maxPrice)
                    bundle.putString(AppConstants.IntentKey.FILTER_IDS, filterString)
                    val intent = Intent()
                    intent.putExtras(bundle)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                R.id.txtReset ->{
                    resetFilter()
                    ToastHelper.showSnackBar(
                        mContext,
                        "Filter reset successfully.",
                        binding.root
                    )
                }
            }
        }

    }

    private fun setFilterAdapter(list: MutableList<ModuleInfo>?) {
        if (list != null && list.size > 0) {
            binding.rvFilters.visibility = View.VISIBLE
            binding.dividerFilters.visibility = View.VISIBLE
            binding.rvFilters.setHasFixedSize(true)
            adapter = FilterItemAdapter(mContext, list, this)
            binding.rvFilters.adapter = adapter
            val layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            binding.rvFilters.layoutManager = layoutManager
        } else {
            binding.rvFilters.visibility = View.GONE
            binding.dividerFilters.visibility = View.GONE
        }
    }

    private fun setRangeTextView(min: Int, max: Int) {
        binding.tvPriceRange.text = "$min-$max"
    }

    override fun onSelectSubItem(position: Int, parentPosition: Int, action: Int) {
        listFilters[parentPosition].values[position].check =
            !listFilters[parentPosition].values[position].check
    }

    private fun resetFilter() {
        if (listFilters.isNotEmpty()) {
            for (i in 0 until listFilters.size) {
                for (j in 0 until listFilters[i].values.size) {
                    listFilters[i].values[j].check = false
                }
            }
            setFilterAdapter(listFilters)
            minPrice = 0
            maxPrice = 15000
            setRangeTextView(minPrice, maxPrice)
            binding.priceRange.setMinStartValue(minPrice.toFloat())
                .setMaxStartValue(maxPrice.toFloat())
            binding.priceRange.apply()
        }

    }
}