package com.ecommerce.albeli.dashboard.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.callback.SelectItemListener
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.common.utils.AppUtils.setImage
import com.ecommerce.albeli.databinding.RowDashboardBannerPagerBinding
import com.ecommerce.imagepickers.utils.Constant

class DashboardBannerPagerAdapter(private val mContext: Context, private val list: List<String>,private val listener:SelectItemListener?) :
    PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater =
            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: RowDashboardBannerPagerBinding = DataBindingUtil.inflate(
            layoutInflater!!,
            R.layout.row_dashboard_banner_pager,
            container,
            false
        )
        container.addView(binding.root)
        setImage(
            mContext, list[position], binding.imgBanner,
            Constant.ImageScaleType.FIT_CENTER
        )
        binding.routMainView.setOnClickListener { v ->
            listener?.onSelectItem(position,AppConstants.Action.VIEW_IMAGE,0)
        }
        return binding.root
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}