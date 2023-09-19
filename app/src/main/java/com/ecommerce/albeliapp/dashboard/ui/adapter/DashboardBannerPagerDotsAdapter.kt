package com.ecommerce.albeliapp.dashboard.data.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.databinding.RowDashboardBannerPagerDotsBinding

class DashboardBannerPagerDotsAdapter(private val mContext: Context, private val totalSize: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var position = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_dashboard_banner_pager_dots, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        if (this.position == position) {
            itemViewHolder.binding.imgDot.setColorFilter(mContext.resources.getColor(R.color.colorDefaultAccent))
        } else {
            itemViewHolder.binding.imgDot.setColorFilter(mContext.resources.getColor(R.color.gray))
        }
    }

    override fun getItemCount(): Int {
        return totalSize
    }

    inner class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        val binding: RowDashboardBannerPagerDotsBinding = DataBindingUtil.bind(itemView!!)!!
    }

    fun setSelectedDot(position: Int) {
        this.position = position
        notifyDataSetChanged()
    }

}
