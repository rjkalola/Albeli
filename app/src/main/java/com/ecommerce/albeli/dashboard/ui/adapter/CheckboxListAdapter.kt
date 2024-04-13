package com.ecommerce.albeli.dashboard.data.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeli.R
import com.ecommerce.albeli.dashboard.data.model.ProductOptionsItemInfo
import com.ecommerce.albeli.databinding.RowCheckboxItemBinding

class CheckboxListAdapter(
    var mContext: Context,
    var list: MutableList<ProductOptionsItemInfo>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_checkbox_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val info = list[position]
        itemViewHolder.getData(info)
        itemViewHolder.binding.checkbox.isChecked = info.check
        itemViewHolder.binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                info.check = isChecked
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
        var binding: RowCheckboxItemBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: ProductOptionsItemInfo?) {
            binding.info = info
        }

    }
}