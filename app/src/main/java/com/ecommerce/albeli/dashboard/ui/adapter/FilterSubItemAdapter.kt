package com.ecommerce.albeli.dashboard.data.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.api.model.ModuleInfo
import com.ecommerce.albeli.dashboard.callback.SelectSubItemListener
import com.ecommerce.albeli.databinding.RowFilterSubItemBinding


class FilterSubItemAdapter(
    var mContext: Context,
    var list: MutableList<ModuleInfo>,
    var listener: SelectSubItemListener,
    var parentPosition: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_filter_sub_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val info = list[position]
        itemViewHolder.binding.checkItem.isChecked = info.check
        itemViewHolder.binding.checkItem.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed)
                listener.onSelectSubItem(position, parentPosition, 0)
        }
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
        var binding: RowFilterSubItemBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: ModuleInfo?) {
            binding.info = info
        }

    }
}