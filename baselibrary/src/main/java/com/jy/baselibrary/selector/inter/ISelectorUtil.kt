package com.jy.baselibrary.selector.inter

import android.view.View
import com.jy.baselibrary.selector.XSelector

interface ISelectorUtil<T, V : View> {
    /**
     * 目标view
     * @param v 需要设置样式的view
     */
    fun into(view: V): XSelector

    fun build(): T
}