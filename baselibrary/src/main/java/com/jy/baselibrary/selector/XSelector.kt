package com.jy.baselibrary.selector

import android.app.Application
import android.content.Context
import com.jy.baselibrary.selector.selector.ColorSelector
import com.jy.baselibrary.selector.selector.CompoundDrawableSelector
import com.jy.baselibrary.selector.selector.DrawableSelector
import com.jy.baselibrary.selector.selector.ShapeSelector

object XSelector {
    private var context: Application? = null

    /**
     * 必须在全局Application先调用，获取context上下文
     * @param app Application
     */
    fun init(app: Application) {
        context = app
    }

    /**
     * 获取上下文
     */
    fun getContext(): Context {
        initialize()
        return context!!
    }

    /**
     * 检测是否调用初始化方法
     */
    private fun initialize() {
        if (context == null) {
            throw ExceptionInInitializerError("请先在全局Application中调用XSelector.init()初始化！")
        }
    }

    /**
     * 设置样式（主要是椭圆和矩形）
     */
    fun shapeSelector(): ShapeSelector = ShapeSelector.getInstance()

    /**
     * 阴影工具类
     */
    fun shadowHelper(): ShadowHelper = ShadowHelper.getInstance()

    /**
     * Color字体颜色选择器
     */
    fun colorSelector(): ColorSelector = ColorSelector.getInstance()


    /**
     * Drawable背景选择器
     */
    fun drawableSelector(): DrawableSelector = DrawableSelector.getInstance()

    /**
     * Drawable方位选择器
     */
    fun compoundDrawableSelector(): CompoundDrawableSelector =
        CompoundDrawableSelector.getInstance()
}