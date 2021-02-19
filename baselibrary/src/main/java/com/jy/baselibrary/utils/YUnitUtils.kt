package com.jy.baselibrary.utils

import android.view.ViewGroup.LayoutParams

/**
 * @Author jy
 * @Date 2019/8/8-16:21
 * @TODO 单位转换
 */
object YUnitUtils {

    fun dp2px(dipValue: Float): Int {
        if (LayoutParams.MATCH_PARENT.toFloat() == dipValue) {
            return LayoutParams.MATCH_PARENT
        }

        if (LayoutParams.WRAP_CONTENT.toFloat() == dipValue) {
            return LayoutParams.WRAP_CONTENT
        }
        val scale = BaseUtils.getApp().resources?.displayMetrics?.density ?: return dipValue.toInt()
        return (dipValue * scale + 0.5f).toInt()
    }


    fun px2dp(pxValue: Float): Int {
        val scale = BaseUtils.getApp().resources?.displayMetrics?.density ?: return pxValue.toInt()
        return (pxValue / scale + 0.5f).toInt()
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    fun px2sp(pxValue: Float): Int {
        val scaledDensity =
            BaseUtils.getApp().resources?.displayMetrics?.scaledDensity ?: return pxValue.toInt()
        return (pxValue / scaledDensity + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    fun sp2px(spValue: Float): Int {
        val scaledDensity =
            BaseUtils.getApp().resources?.displayMetrics?.scaledDensity ?: return spValue.toInt()
        return (spValue * scaledDensity + 0.5f).toInt()
    }

}
