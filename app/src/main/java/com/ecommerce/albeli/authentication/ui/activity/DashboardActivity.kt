package com.ecommerce.albeli.authentication.ui.activity


import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeli.R
import com.ecommerce.albeli.common.ui.activity.BaseActivity
import com.ecommerce.albeli.databinding.ActivityDashboardBinding


class DashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var mContext: Context;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        setStatusBarColor()
        mContext = this
    }

}