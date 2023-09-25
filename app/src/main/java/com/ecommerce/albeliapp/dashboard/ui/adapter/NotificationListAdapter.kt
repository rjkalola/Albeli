package com.ecommerce.albeliapp.dashboard.data.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.dashboard.data.model.NotificationInfo
import com.ecommerce.albeliapp.databinding.RowNotificationBinding

class NotificationListAdapter(
    var mContext: Context,
    var list: MutableList<NotificationInfo>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listAll: MutableList<NotificationInfo> = ArrayList()

    init {
        this.listAll.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_notification, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val info = list[position]
        itemViewHolder.getData(info)
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
        var binding: RowNotificationBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: NotificationInfo?) {
            binding.info = info
        }

    }

    fun addData(list: MutableList<NotificationInfo>) {
        listAll.addAll(list)
        this.list.clear()
        this.list.addAll(listAll)
        notifyDataSetChanged()
    }
}