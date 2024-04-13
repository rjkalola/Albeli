package com.ecommerce.albeli.dashboard.data.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.callback.SelectItemListener
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.dashboard.data.model.AddressInfo
import com.ecommerce.albeli.databinding.RowAddressListBinding
import com.ecommerce.utilities.utils.StringHelper

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
        itemViewHolder.binding.tvName.text = info.first_name + " " + info.last_name
        val list: MutableList<String> = ArrayList()
        if (!StringHelper.isEmpty(info.address_1))
            list.add(info.address_1)
        if (!StringHelper.isEmpty(info.address_2))
            list.add(info.address_2)
        if (!StringHelper.isEmpty(info.city))
            list.add(info.city)
        if (!StringHelper.isEmpty(info.state))
            list.add(info.state)
        if (!StringHelper.isEmpty(info.zip))
            list.add(info.zip)
        itemViewHolder.binding.tvAddress.text = TextUtils.join(",", list)
        if(info.is_default == "1"){
            itemViewHolder.binding.ivDone.setBackgroundResource(R.drawable.ic_circle_default_address_pressed)
        }else {
            itemViewHolder.binding.ivDone.setBackgroundResource(R.drawable.ic_circle_default_address_normal)
        }
        itemViewHolder.binding.tvEdit.setOnClickListener {
            listener.onSelectItem(position, AppConstants.Action.ADDRESS_DETAILS, 0)
        }
        itemViewHolder.binding.ivDone.setOnClickListener {
            listener.onSelectItem(position, AppConstants.Action.CHANGE_DEFAULT_ADDRESS, 0)
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
        var binding: RowAddressListBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: AddressInfo?) {
            binding.info = info
        }

    }
}