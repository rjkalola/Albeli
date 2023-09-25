package com.ecommerce.albeliapp.dashboard.data.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.common.callback.SelectItemListener
import com.ecommerce.albeliapp.dashboard.data.model.AddressInfo
import com.ecommerce.albeliapp.databinding.RowAddressListBinding

class AddressListAdapter(
    var mContext: Context,
    var list: MutableList<AddressInfo>,
    var listener: SelectItemListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_address_list, parent, false)
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
        var binding: RowAddressListBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: AddressInfo?) {
            binding.info = info
        }

    }
}