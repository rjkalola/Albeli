package com.ecommerce.albeli.dashboard.data.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.callback.SelectItemListener
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.common.utils.AppUtils
import com.ecommerce.albeli.dashboard.data.model.CategoryProductInfo
import com.ecommerce.albeli.databinding.RowCartBinding
import com.ecommerce.imagepickers.utils.Constant
import java.lang.String
import java.util.Locale

class CartListAdapter(
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
                .inflate(R.layout.row_cart, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val info = list[position]
        itemViewHolder.getData(info)

        AppUtils.setImage(
            mContext, info.product_image, itemViewHolder.binding.imgProduct,
            Constant.ImageScaleType.CENTER_CROP
        )
        itemViewHolder.binding.tvItemCount.text = info.qty.toString()
        itemViewHolder.binding.tvItemPrice.text = "₹" + String.format(
            Locale.getDefault(),
            "%.02f",
            info.unit_price)

        itemViewHolder.binding.ivDecrease.setOnClickListener {
            listener.onSelectItem(position, AppConstants.Action.DECREASE_QTY, 0)
        }

        itemViewHolder.binding.ivIncrease.setOnClickListener {
            listener.onSelectItem(position, AppConstants.Action.INCREASE_QTY, 0)
        }

        itemViewHolder.binding.ivDelete.setOnClickListener {
            listener.onSelectItem(position, AppConstants.Action.REMOVE_CART_ITEM, 0)
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
        var binding: RowCartBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: CategoryProductInfo?) {
            binding.info = info
        }

    }
}