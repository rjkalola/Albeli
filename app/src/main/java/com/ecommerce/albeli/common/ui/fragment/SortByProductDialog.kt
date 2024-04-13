package com.ecommerce.albeli.common.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.callback.SelectItemListener
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.databinding.DialogSortByProductBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SortByProductDialog(context: Context?) :
    BottomSheetDialog(context!!), View.OnClickListener {
    private lateinit var binding: DialogSortByProductBinding

    companion object {
        private var mListener: SelectItemListener? = null
        private var selectedItem: Int? = 0
        private var identifyId: Int? = 0

        fun newInstance(
            context: Context?,
            selectedItem: Int? = 0,
            identifyId: Int? = 0,
            mListener: SelectItemListener?
        ): SortByProductDialog {
            this.selectedItem = selectedItem!!
            this.identifyId = identifyId!!
            this.mListener = mListener
            return SortByProductDialog(context!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sheetView: View = layoutInflater.inflate(R.layout.dialog_sort_by_product, null)
        setContentView(sheetView)
        binding = DataBindingUtil.bind(sheetView)!!
        binding.routNameAscending.setOnClickListener(this)
        binding.routNameDescending.setOnClickListener(this)
        binding.routPriceHighToLow.setOnClickListener(this)
        binding.routPriceLowToHigh.setOnClickListener(this)
        binding.routDateModified.setOnClickListener(this)

        setSelectedItem(selectedItem!!)
    }

    override fun onClick(v: View?) {
        var sort = 0
        when (v?.id) {
            R.id.routNameAscending -> sort = AppConstants.SortBy.PRODUCT_NAME_A_TO_Z
            R.id.routNameDescending -> sort = AppConstants.SortBy.PRODUCT_NAME_Z_To_A
            R.id.routPriceLowToHigh -> sort = AppConstants.SortBy.PRODUCT_PRICE_LOW_TO_HIGH
            R.id.routPriceHighToLow -> sort = AppConstants.SortBy.PRODUCT_PRICE_HIGH_TO_LOW
            R.id.routDateModified -> sort = AppConstants.SortBy.PRODUCT_DATE_MODIFIED
        }
        mListener?.onSelectItem(sort, identifyId!!,0)
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        this.window!!.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }

    fun setSelectedItem(selectedItem: Int) {
        binding.imgNameAscending.visibility = View.GONE
        binding.imgNameDescending.visibility = View.GONE
        binding.imgPriceLowToHigh.visibility = View.GONE
        binding.imgPriceHighToLow.visibility = View.GONE
        binding.imgDateModified.visibility = View.GONE

        when (selectedItem) {
            AppConstants.SortBy.PRODUCT_NAME_A_TO_Z -> binding.imgNameAscending.visibility =
                View.VISIBLE
            AppConstants.SortBy.PRODUCT_NAME_Z_To_A -> binding.imgNameDescending.visibility =
                View.VISIBLE
            AppConstants.SortBy.PRODUCT_PRICE_LOW_TO_HIGH -> binding.imgPriceLowToHigh.visibility =
                View.VISIBLE
            AppConstants.SortBy.PRODUCT_PRICE_HIGH_TO_LOW -> binding.imgPriceHighToLow.visibility =
                View.VISIBLE
            AppConstants.SortBy.PRODUCT_DATE_MODIFIED -> binding.imgDateModified.visibility =
                View.VISIBLE
        }
    }


}