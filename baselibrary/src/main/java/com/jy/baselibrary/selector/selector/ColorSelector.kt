package com.jy.baselibrary.selector.selector


import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.TextView
import androidx.annotation.ColorRes
import com.jy.baselibrary.selector.XSelector
import com.jy.baselibrary.selector.XSelectorHelper
import com.jy.baselibrary.selector.inter.ISelectorUtil


/**

 * @Author Administrator
 * @Date 2019/10/21-11:54
 * @TODO
 */
class ColorSelector private constructor() : ISelectorUtil<ColorStateList, TextView> {
    companion object {
        const val TEXT_COLOR = 1
        const val HINT_TEXT_COLOR = 2

        fun getInstance() = ColorSelector()
    }

    private var textType = TEXT_COLOR

    //不可点击颜色
    private var disabledColor: Int = 0
    //获得焦点的颜色
    private var focusedColor: Int = 0
    //触摸颜色
    private var pressedColor: Int = 0
    //触摸颜色
    private var selectedColor: Int = 0
    //正常颜色
    private var normalColor: Int = 0

    private var hasSetDisabledColor = false
    private var hasSetPressedColor = false
    private var hasSetSelectedColor = false
    private var hasSetFocusedColor = false

    fun defaultColor(@ColorRes tmpColor: Int): ColorSelector {
        val color = XSelectorHelper.getColorRes(tmpColor)
        normalColor = color
        if (!hasSetDisabledColor)
            disabledColor = color
        if (!hasSetPressedColor)
            pressedColor = color
        if (!hasSetSelectedColor)
            selectedColor = color
        if (!hasSetFocusedColor)
            focusedColor = color
        return this
    }

    fun defaultColor(tmpColor: String): ColorSelector {
        val color = Color.parseColor(tmpColor)
        normalColor = color
        if (!hasSetDisabledColor)
            disabledColor = color
        if (!hasSetPressedColor)
            pressedColor = color
        if (!hasSetSelectedColor)
            selectedColor = color
        if (!hasSetFocusedColor)
            focusedColor = color
        return this
    }

    fun disabledColor(@ColorRes color: Int): ColorSelector {
        disabledColor = XSelectorHelper.getColorRes(color)
        hasSetDisabledColor = true
        return this
    }

    fun disabledColor(color: String): ColorSelector {
        disabledColor = Color.parseColor(color)
        hasSetDisabledColor = true
        return this
    }

    fun pressedColor(@ColorRes color: Int): ColorSelector {
        pressedColor = XSelectorHelper.getColorRes(color)
        hasSetPressedColor = true
        return this
    }

    fun pressedColor(color: String): ColorSelector {
        pressedColor = Color.parseColor(color)
        hasSetPressedColor = true
        return this
    }

    fun selectedColor(@ColorRes color: Int): ColorSelector {
        selectedColor = XSelectorHelper.getColorRes(color)
        hasSetSelectedColor = true
        return this
    }

    fun selectedColor(color: String): ColorSelector {
        selectedColor = Color.parseColor(color)
        hasSetSelectedColor = true
        return this
    }

    fun focusedColor(@ColorRes color: Int): ColorSelector {
        focusedColor = XSelectorHelper.getColorRes(color)
        hasSetFocusedColor = true
        return this
    }

    fun focusedColor(color: String): ColorSelector {
        focusedColor = Color.parseColor(color)
        hasSetFocusedColor = true
        return this
    }


    fun textType(type: Int): ColorSelector {
        textType = type
        return this
    }

    override fun into(view: TextView): XSelector {
        if (hasSetPressedColor) {
            view.isClickable = true
        }
        if (HINT_TEXT_COLOR == textType) {
            view.setHintTextColor(create())
        } else {
            view.setTextColor(create())
        }
        return XSelector
    }

    override fun build(): ColorStateList = create()

    /**
     * 创建触摸颜色变化
     */
    private fun create(): ColorStateList {
        val colors = intArrayOf(
            if (hasSetDisabledColor) disabledColor else normalColor,
            if (hasSetPressedColor) pressedColor else normalColor,
            if (hasSetSelectedColor) selectedColor else normalColor,
            if (hasSetFocusedColor) focusedColor else normalColor,
            normalColor
        )

        return getColorStateList(colors)
    }

    private fun getColorStateList(colors: IntArray): ColorStateList {
        val states = arrayOfNulls<IntArray>(5)
        states[0] = intArrayOf(-android.R.attr.state_enabled)
        states[1] = intArrayOf(android.R.attr.state_pressed)
        states[2] = intArrayOf(android.R.attr.state_selected)
        states[3] = intArrayOf(android.R.attr.state_focused)
        states[4] = intArrayOf()
        return ColorStateList(states, colors)
    }
}