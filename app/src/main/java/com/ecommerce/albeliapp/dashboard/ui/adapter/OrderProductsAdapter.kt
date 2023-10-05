package com.ecommerce.albeliapp.dashboard.data.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeliapp.R
import com.ecommerce.albeliapp.dashboard.data.model.OrderProductInfo
import com.ecommerce.albeliapp.databinding.RowOrderProductBinding


class OrderProductsAdapter(
    var mContext: Context,
    var list: MutableList<OrderProductInfo>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_order_product, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val info = list[position]
        itemViewHolder.getData(info)

        itemViewHolder.binding.tvQty.text = info.qty.toString()
        if (info.options.isNotEmpty()) {
            itemViewHolder.binding.llSize.visibility = View.VISIBLE

            itemViewHolder.binding.tvSizeName.text = info.options[0].opt_name + ":"
            itemViewHolder.binding.tvSize.text = info.options[0].opt_lable
        } else {
            itemViewHolder.binding.llSize.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(info.sku))
            itemViewHolder.binding.llSku.visibility = View.VISIBLE
        else
            itemViewHolder.binding.llSku.visibility = View.GONE


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
        var binding: RowOrderProductBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: OrderProductInfo?) {
            binding.info = info
        }

    }
}