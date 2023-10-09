package com.ecommerce.albeliapp.dashboard.data.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.api.model.ModuleInfo
import com.ecommerce.albeliapp.dashboard.callback.SelectSubItemListener
import com.ecommerce.albeliapp.databinding.RowFilterItemBinding

class FilterItemAdapter(
    var mContext: Context,
    var list: MutableList<ModuleInfo>,
    var listener: SelectSubItemListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_filter_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val info = list[position]
        itemViewHolder.getData(info)
        setFilterSubItemsAdapter(itemViewHolder.binding.rvSubItems,info.values,position)
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
        var binding: RowFilterItemBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: ModuleInfo?) {
            binding.info = info
        }

    }

    private fun setFilterSubItemsAdapter(
        recyclerView: RecyclerView,
        list: MutableList<ModuleInfo>,
        position: Int
    ) {
        if (list.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            val adapter = FilterSubItemAdapter(mContext, list, listener, position)
            recyclerView.adapter = adapter
            val layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = layoutManager
        } else {
            recyclerView.visibility = View.GONE
        }
    }
}