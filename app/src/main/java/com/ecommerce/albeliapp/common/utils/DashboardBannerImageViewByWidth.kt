package com.ecommerce.albeliapp.common.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class DashboardBannerImageViewByWidth : AppCompatImageView {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context) : super(context) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = (width/2.82f).toInt()
        setMeasuredDimension(width, height)
    }
}