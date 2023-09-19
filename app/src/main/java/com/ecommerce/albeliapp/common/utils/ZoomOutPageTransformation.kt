package com.ecommerce.albeliapp.common.utils

import android.view.View
import androidx.viewpager.widget.ViewPager


class ZoomOutPageTransformation : ViewPager.PageTransformer {
    override fun transformPage(view: View, f: Float) {
        val width: Int = view.width
        val height: Int = view.height
        if (f < -1.0f) {
            view.alpha = 0.0f
        } else if (f <= 1.0f) {
            val max = Math.max(MIN_SCALE, 1.0f - Math.abs(f))
            val f2 = 1.0f - max
            val f3 = height.toFloat() * f2 / 2.0f
            val f4 = width.toFloat() * f2 / 2.0f
            if (f < 0.0f) {
                view.translationX = f4 - f3 / 2.0f
            } else {
                view.translationX = -f4 + f3 / 2.0f
            }
            view.scaleX = max
            view.scaleY = max
            view.alpha = (max - MIN_SCALE) / 0.14999998f * 0.19999999f + MIN_ALPHA
        } else {
            view.alpha = 0.0f
        }
    }

    companion object {
        private const val MIN_ALPHA = 0.8f
        private const val MIN_SCALE = 0.85f
    }
}