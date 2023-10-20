package com.ecommerce.albeliapp.dashboard.data.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.callback.SelectItemListener
import com.ecommerce.albeliapp.common.utils.AppConstants
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductInfo
import com.ecommerce.albeliapp.databinding.RowCategoryProductBinding
import com.ecommerce.imagepickers.utils.Constant
import com.ecommerce.utilities.utils.NetworkHelper
import com.ecommerce.utilities.utils.ToastHelper

class CategoryProductsAdapter(
    var mContext: Context,
    var list: MutableList<CategoryProductInfo>,
    var listener: SelectItemListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listAll: MutableList<CategoryProductInfo> = ArrayList()

    init {
        this.listAll.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_category_product, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val info = list[position]
        itemViewHolder.getData(info)
        AppUtils.setImage(
            mContext, info.path, itemViewHolder.binding.imgProduct,
            Constant.ImageScaleType.FIT_CENTER
        )
        itemViewHolder.binding.txtOldPrice.paintFlags =
            itemViewHolder.binding.txtOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        itemViewHolder.binding.imgFavorite.isChecked = info.wishlisted;
        itemViewHolder.binding.routMainView.setOnClickListener {
            listener.onSelectItem(position, AppConstants.Action.PRODUCTS_DETAILS, 0)
        }
        itemViewHolder.binding.imgFavorite.setOnClickListener {
            listener.onSelectItem(position, AppConstants.Action.WATCHLIST, 0)
            if (NetworkHelper.isConnected(mContext) && AppUtils.isLogin(mContext)) {
                info.wishlisted = !info.wishlisted;
                itemViewHolder.binding.imgFavorite.isChecked = info.wishlisted;
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var binding: RowCategoryProductBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: CategoryProductInfo?) {
            binding.info = info
        }

    }

    fun addData(list: MutableList<CategoryProductInfo>) {
        listAll.addAll(list)
        this.list.clear()
        this.list.addAll(listAll)
        notifyDataSetChanged()
    }
}