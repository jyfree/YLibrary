package com.jy.baselibrary.utils

import android.content.Context
import android.view.ViewGroup.LayoutParams

/**
 * @Author jy
 * @Date 2019/8/8-16:21
 * @TODO 单位转换
 */
object YUnitUtils {

    fun dp2px(context: Context?, dipValue: Float): Int {
        if (context != null) {

            if (LayoutParams.MATCH_PARENT.toFloat() == dipValue) {
                return LayoutParams.MATCH_PARENT
            }

            if (LayoutParams.WRAP_CONTENT.toFloat() == dipValue) {
                return LayoutParams.WRAP_CONTENT
            }

            val scale = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }
        return dipValue.toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    fun px2sp(pxValue: Float, context: Context): Int {
        return (pxValue / context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    fun sp2px(spValue: Float, context: Context): Int {
        return (spValue * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }

    fun px2dp(context: Context?, pxValue: Float): Int {
        if (context != null) {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }
        return pxValue.toInt()
    }


}
