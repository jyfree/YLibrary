package com.jy.baselibrary.selector.selector

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringDef
import com.jy.baselibrary.selector.XSelector
import com.jy.baselibrary.selector.XSelectorHelper.dip2px
import com.jy.baselibrary.selector.inter.ISelectorUtil

/**

 * @Author Administrator
 * @Date 2019/10/21-16:33
 * @TODO
 */
class CompoundDrawableSelector private constructor() : ISelectorUtil<Drawable, TextView> {
    companion object {
        const val LEFT = "LEFT"
        const val RIGHT = "RIGHT"
        const val TOP = "TOP"
        const val BOTTOM = "BOTTOM"

        fun getInstance() = CompoundDrawableSelector()
    }

    private val drawableSelector = DrawableSelector.getInstance()

    private var drawablePadding = 0f //绘制间隔
    @DrawableOrientation
    private var drawableOrientation = LEFT

    @StringDef(LEFT, RIGHT, TOP, BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class DrawableOrientation

    //是否设置TextView颜色选择器
    private var isSelectorColor: Boolean = false
    private var mColorStateList: ColorStateList? = null

    fun defaultDrawable(@DrawableRes drawableRes: Int): CompoundDrawableSelector {
        drawableSelector.defaultDrawable(drawableRes)
        return this
    }

    fun disabledDrawable(@DrawableRes drawableRes: Int): CompoundDrawableSelector {
        drawableSelector.disabledDrawable(drawableRes)
        return this
    }

    fun pressedDrawable(@DrawableRes drawableRes: Int): CompoundDrawableSelector {
        drawableSelector.pressedDrawable(drawableRes)
        return this
    }

    fun selectedDrawable(@DrawableRes drawableRes: Int): CompoundDrawableSelector {
        drawableSelector.selectedDrawable(drawableRes)
        return this
    }

    fun focusedDrawable(@DrawableRes drawableRes: Int): CompoundDrawableSelector {
        drawableSelector.focusedDrawable(drawableRes)
        return this
    }

    fun setDrawablePadding(drawablePadding: Float): CompoundDrawableSelector {
        this.drawablePadding = drawablePadding
        return this
    }

    /**
     * 设置绘制方向 例：CompoundDrawableSelector.TOP
     */
    fun setDrawableOrientation(@DrawableOrientation drawableOrientation: String): CompoundDrawableSelector {
        this.drawableOrientation = drawableOrientation
        return this
    }

    fun defaultDrawable(drawable: Drawable): CompoundDrawableSelector {
        drawableSelector.defaultDrawable(drawable)
        return this
    }

    fun disabledDrawable(drawable: Drawable): CompoundDrawableSelector {
        drawableSelector.disabledDrawable(drawable)
        return this
    }

    fun pressedDrawable(drawable: Drawable): CompoundDrawableSelector {
        drawableSelector.pressedDrawable(drawable)
        return this
    }

    fun selectedDrawable(drawable: Drawable): CompoundDrawableSelector {
        drawableSelector.selectedDrawable(drawable)
        return this
    }

    fun focusedDrawable(drawable: Drawable): CompoundDrawableSelector {
        drawableSelector.focusedDrawable(drawable)
        return this
    }

    /**
     * 设置背景选择器同时设置字体颜色颜色器
     *
     * @param pressedColorResId 触摸颜色 例：R.color.colorPrimary
     * @param normalColorResId  正常颜色 例：R.color.colorPrimary
     */
    fun selectorColor(@ColorRes pressedColorResId: Int, @ColorRes normalColorResId: Int): CompoundDrawableSelector {
        mColorStateList = ColorSelector.getInstance()
            .pressedColor(pressedColorResId)
            .defaultColor(normalColorResId)
            .build()
        this.isSelectorColor = true
        return this
    }

    /**
     * 设置背景选择器同时设置字体颜色颜色器
     *
     * @param pressedColor 触摸颜色 例：#ffffff
     * @param normalColor  正常颜色 例：#ffffff
     */
    fun selectorColor(pressedColor: String, normalColor: String): CompoundDrawableSelector {
        mColorStateList = ColorSelector.getInstance()
            .pressedColor(pressedColor)
            .defaultColor(normalColor)
            .build()
        this.isSelectorColor = true
        return this
    }

    override fun into(view: TextView): XSelector {
        try {
            view.isClickable = true
            val drawable = build()
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            view.compoundDrawablePadding = dip2px(drawablePadding)
            when (drawableOrientation) {
                LEFT -> view.setCompoundDrawables(drawable, null, null, null)
                RIGHT -> view.setCompoundDrawables(null, null, drawable, null)
                TOP -> view.setCompoundDrawables(null, drawable, null, null)
                BOTTOM -> view.setCompoundDrawables(null, null, null, drawable)
            }
            if (isSelectorColor) {
                view.setTextColor(mColorStateList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw ExceptionInInitializerError("set compound drawable 必须是TextView或其子类！")
        }
        return XSelector
    }

    override fun build(): Drawable = drawableSelector.build()
}