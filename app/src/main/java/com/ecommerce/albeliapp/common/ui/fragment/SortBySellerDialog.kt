package com.ecommerce.albeliapp.common.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.callback.SelectItemListener
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.databinding.DialogSortBySellerBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SortBySellerDialog(context: Context?) :
    BottomSheetDialog(context!!), View.OnClickListener {
    private lateinit var binding: DialogSortBySellerBinding

    companion object {
        private var mListener: SelectItemListener? = null
        private var selectedItem: Int? = 0
        private var identifyId: Int? = 0

        fun newInstance(
            context: Context?,
            selectedItem: Int? = 0,
            identifyId: Int? = 0,
            mListener: SelectItemListener?
        ): SortBySellerDialog {
            this.selectedItem = selectedItem!!
            this.identifyId = identifyId!!
            this.mListener = mListener
            return SortBySellerDialog(context!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sheetView: View = layoutInflater.inflate(R.layout.dialog_sort_by_seller, null)
        setContentView(sheetView)
        binding = DataBindingUtil.bind(sheetView)!!
        binding.routNameAscending.setOnClickListener(this)
        binding.routNameDescending.setOnClickListener(this)
        binding.routDateModified.setOnClickListener(this)

        setSelectedItem(selectedItem!!)
    }

    override fun onClick(v: View?) {
        var sort = 0
        when (v?.id) {
            R.id.routNameAscending -> sort = AppConstants.SortBy.SELLER_NAME_A_TO_Z
            R.id.routNameDescending -> sort = AppConstants.SortBy.SELLER_NAME_Z_To_A
            R.id.routDateModified -> sort = AppConstants.SortBy.SELLER_DATE_MODIFIED
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
        binding.imgDateModified.visibility = View.GONE

        when (selectedItem) {
            AppConstants.SortBy.SELLER_NAME_A_TO_Z -> binding.imgNameAscending.visibility =
                View.VISIBLE
            AppConstants.SortBy.SELLER_NAME_Z_To_A -> binding.imgNameDescending.visibility =
                View.VISIBLE
            AppConstants.SortBy.SELLER_DATE_MODIFIED -> binding.imgDateModified.visibility =
                View.VISIBLE
        }
    }


}