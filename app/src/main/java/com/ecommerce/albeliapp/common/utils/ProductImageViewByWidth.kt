package com.ecommerce.albeliapp.common.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView


class ProductImageViewByWidth : AppCompatImageView {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context) : super(context) {}

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val width = measuredWidth
//        val height = (width*1.78).toInt()
//        setMeasuredDimension(width, height)
//    }
        
    override fun onMeasure(i: Int, i2: Int) {
        val drawable = drawable
        if (drawable != null) {
            val size: Int = View.MeasureSpec.getSize(i)
            setMeasuredDimension(size, drawable.intrinsicHeight * size / drawable.intrinsicWidth)
            return
        }
        super.onMeasure(i, i2)
    }
}