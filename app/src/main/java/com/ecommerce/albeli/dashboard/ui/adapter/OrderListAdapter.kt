package com.ecommerce.albeli.dashboard.data.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.dashboard.data.model.OrderInfo
import com.ecommerce.albeli.dashboard.ui.activity.OrderDetailsActivity
import com.ecommerce.albeli.databinding.RowOrderListBinding

class OrderListAdapter(
    var mContext: Context,
    var list: MutableList<OrderInfo>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listAll: MutableList<OrderInfo> = ArrayList()

    init {
        this.listAll.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_order_list, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val info = list[position]
        itemViewHolder.getData(info)
        itemViewHolder.binding.tvItemId.text = "#" + info.id.toString()
        itemViewHolder.binding.routMainView.setOnClickListener {
            val intent = Intent(mContext, OrderDetailsActivity::class.java)
            intent.putExtra(AppConstants.IntentKey.ORDER_ID,info.id.toString())
            mContext.startActivity(intent)
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
        var binding: RowOrderListBinding = DataBindingUtil.bind(itemView)!!
        fun getData(info: OrderInfo?) {
            binding.info = info
        }

    }

    fun addData(list: MutableList<OrderInfo>) {
        listAll.addAll(list)
        this.list.clear()
        this.list.addAll(listAll)
        notifyDataSetChanged()
    }
}