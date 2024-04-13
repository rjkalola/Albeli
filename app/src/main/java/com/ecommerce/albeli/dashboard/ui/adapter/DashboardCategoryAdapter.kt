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
import com.ecommerce.albeli.dashboard.data.model.DashboardCategoryInfo
import com.ecommerce.albeli.databinding.RowDashboardCategoryItemBinding
import com.ecommerce.imagepickers.utils.Constant

class DashboardCategoryAdapter(
    var mContext: Context,
    var list: MutableList<DashboardCategoryInfo>,
    var listener: SelectItemListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_dashboard_category_item, parent, false)
        return ItemViewHolder(itemView)
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val info = list[position]
        itemViewHolder.getData(info)
        AppUtils.setImage(mContext,info.image,itemViewHolder.binding.imgProduct,
            Constant.ImageScaleType.FIT_CENTER)

        itemViewHolder.binding.routMainView.setOnClickListener {
            listener.onSelectItem(position, AppConstants.Action.CATEGORY_DETAILS, 0)
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
        var binding: RowDashboardCategoryItemBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: DashboardCategoryInfo?) {
            binding.info = info
        }

    }
}