package com.jy.baselibrary.selector.selector

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.*
import com.jy.baselibrary.selector.XSelector
import com.jy.baselibrary.selector.XSelectorHelper
import com.jy.baselibrary.selector.XSelectorHelper.dip2px
import com.jy.baselibrary.selector.inter.ISelectorUtil


class ShapeSelector private constructor() : ISelectorUtil<Drawable, View> {
    companion object {
        /*线性渐变方向定义*/
        //上到下渐变
        const val TOP_BOTTOM = "TOP_BOTTOM"
        //右上到左下渐变
        const val TR_BL = "TR_BL"
        //右到左渐变
        const val RIGHT_LEFT = "RIGHT_LEFT"
        //右下到左上渐变
        const val BR_TL = "BR_TL"
        //下到上渐变
        const val BOTTOM_TOP = "BOTTOM_TOP"
        //左下到右上渐变
        const val BL_TR = "BL_TR"
        //左到右渐变
        const val LEFT_RIGHT = "LEFT_RIGHT"
        //左上到右下渐变
        const val TL_BR = "TL_BR"

        fun getInstance(): ShapeSelector = ShapeSelector()
    }

    /*虚线参数*/
    //虚线宽度 默认1px
    private var dashLineWidth = 1
    //虚线颜色
    private var dashLineColor = 1
    //虚线宽度
    private var dashWidth = 1f
    //虚线间隙宽度
    private var dashGap = 1f

    /*渐变参数*/
    //渐变颜色数组
    private var gradientColors: IntArray? = null
    //辐射渐变半径
    private var radialRadius: Float = 0.toFloat()
    //渐变类型  默认线性
    private var gradientType = GradientDrawable.LINEAR_GRADIENT
    //渐变方向 默认从上到下
    @GradientOrientation
    private var gradientOrientation = TOP_BOTTOM

    /*设置样式标志位*/
    //是否设置背景
    private var isBackgroundColor: Boolean = false
    //是否边框实线样式
    private var isLine: Boolean = false
    //是否边框虚线样式
    private var isDashLine: Boolean = false
    //是否渐变
    private var isGradient: Boolean = false

    /*设置圆角*/
    //是否圆角
    private var isRadius: Boolean = false
    //右上圆角弧度
    private var topRightRadius = 0f
    //左上圆角弧度
    private var topLeftRadius = 0f
    //右下圆角弧度
    private var bottomRightRadius = 0f
    //左下圆角弧度
    private var bottomLeftRadius = 0f

    @IntDef(
        GradientDrawable.RECTANGLE,
        GradientDrawable.OVAL,
        GradientDrawable.LINE,
        GradientDrawable.RING
    )
    private annotation class Shape

    @StringDef(TOP_BOTTOM, TR_BL, RIGHT_LEFT, BR_TL, BOTTOM_TOP, BL_TR, LEFT_RIGHT, TL_BR)
    @Retention(AnnotationRetention.SOURCE)
    annotation class GradientOrientation

    private var mShape: Int = 0               //the shape of background
    private var mDefaultBgColor: Int = 0      //default background color
    private var mDisabledBgColor: Int = 0     //state_enabled = false
    private var mPressedBgColor: Int = 0      //state_pressed = true
    private var mSelectedBgColor: Int = 0     //state_selected = true
    private var mFocusedBgColor: Int = 0      //state_focused = true
    private var mStrokeWidth: Int = 1         //stroke width in pixel
    private var mDefaultStrokeColor: Int = 0  //default stroke color
    private var mDisabledStrokeColor: Int = 0 //state_enabled = false
    private var mPressedStrokeColor: Int = 0  //state_pressed = true
    private var mSelectedStrokeColor: Int = 0 //state_selected = true
    private var mFocusedStrokeColor: Int = 0  //state_focused = true
    private var mCornerRadius: Int = 0      //corner radius

    private var hasSetDisabledBgColor = false
    private var hasSetPressedBgColor = false
    private var hasSetSelectedBgColor = false
    private var hasSetFocusedBgColor = false

    private var hasSetDisabledStrokeColor = false
    private var hasSetPressedStrokeColor = false
    private var hasSetSelectedStrokeColor = false
    private var hasSetFocusedStrokeColor = false

    //是否设置TextView|button颜色选择器
    private var isSelectorColor: Boolean = false
    private var mColorStateList: ColorStateList? = null

    init {
        mShape = GradientDrawable.RECTANGLE
        mStrokeWidth = 0
        mCornerRadius = 0
    }

    /**
     * 设置背景类型
     * @param shape GradientDrawable.OVAL
     */
    fun setShape(@Shape shape: Int): ShapeSelector {
        mShape = shape
        return this
    }

    /**
     *设置默认背景颜色
     * @param tmpColor R.color.red
     */
    fun defaultBgColor(@ColorRes tmpColor: Int): ShapeSelector {
        val color = XSelectorHelper.getColorRes(tmpColor)
        mDefaultBgColor = color
        isBackgroundColor = true
        if (!hasSetDisabledBgColor)
            mDisabledBgColor = color
        if (!hasSetPressedBgColor)
            mPressedBgColor = color
        if (!hasSetSelectedBgColor)
            mSelectedBgColor = color
        if (!hasSetFocusedBgColor)
            mFocusedBgColor = color
        return this
    }

    /**
     * 设置默认背景颜色颜色
     * @param tmpColor  #FFFF0000
     */
    fun defaultBgColor(tmpColor: String): ShapeSelector {
        val color = Color.parseColor(tmpColor)
        mDefaultBgColor = color
        isBackgroundColor = true
        if (!hasSetDisabledBgColor)
            mDisabledBgColor = color
        if (!hasSetPressedBgColor)
            mPressedBgColor = color
        if (!hasSetSelectedBgColor)
            mSelectedBgColor = color
        if (!hasSetFocusedBgColor)
            mFocusedBgColor = color
        return this
    }

    /**
     * 设置不可点击颜色
     */
    fun disabledBgColor(@ColorRes color: Int): ShapeSelector {
        mDisabledBgColor = XSelectorHelper.getColorRes(color)
        hasSetDisabledBgColor = true
        isBackgroundColor = true
        return this
    }

    fun disabledBgColor(color: String): ShapeSelector {
        mDisabledBgColor = Color.parseColor(color)
        hasSetDisabledBgColor = true
        isBackgroundColor = true
        return this
    }

    /**
     * 设置手指抬起时颜色
     */
    fun pressedBgColor(@ColorRes color: Int): ShapeSelector {
        mPressedBgColor = XSelectorHelper.getColorRes(color)
        hasSetPressedBgColor = true
        isBackgroundColor = true
        return this
    }

    fun pressedBgColor(color: String): ShapeSelector {
        mPressedBgColor = Color.parseColor(color)
        hasSetPressedBgColor = true
        isBackgroundColor = true
        return this
    }

    /**
     * 设置控件选中时颜色
     */
    fun selectedBgColor(@ColorRes color: Int): ShapeSelector {
        mSelectedBgColor = XSelectorHelper.getColorRes(color)
        hasSetSelectedBgColor = true
        isBackgroundColor = true
        return this
    }

    fun selectedBgColor(color: String): ShapeSelector {
        mSelectedBgColor = Color.parseColor(color)
        hasSetSelectedBgColor = true
        isBackgroundColor = true
        return this
    }

    /**
     * 设置获取焦点时的背景色
     */
    fun focusedBgColor(@ColorRes color: Int): ShapeSelector {
        mFocusedBgColor = XSelectorHelper.getColorRes(color)
        isBackgroundColor = true
        hasSetPressedBgColor = true
        return this
    }

    fun focusedBgColor(color: String): ShapeSelector {
        mFocusedBgColor = Color.parseColor(color)
        isBackgroundColor = true
        hasSetPressedBgColor = true
        return this
    }

    /**
     * 设置绘制边框宽度
     */
    fun strokeWidth(@Dimension width: Int): ShapeSelector {
        mStrokeWidth = width
        return this
    }

    /**
     * 设置绘制边框颜色
     */
    fun defaultStrokeColor(@ColorRes tmpColor: Int): ShapeSelector {
        val color = XSelectorHelper.getColorRes(tmpColor)
        mDefaultStrokeColor = color
        isLine = true
        if (!hasSetDisabledStrokeColor)
            mDisabledStrokeColor = color
        if (!hasSetPressedStrokeColor)
            mPressedStrokeColor = color
        if (!hasSetSelectedStrokeColor)
            mSelectedStrokeColor = color
        if (!hasSetFocusedStrokeColor)
            mFocusedStrokeColor = color
        return this
    }

    fun defaultStrokeColor(tmpColor: String): ShapeSelector {
        val color = Color.parseColor(tmpColor)
        mDefaultStrokeColor = color
        isLine = true
        if (!hasSetDisabledStrokeColor)
            mDisabledStrokeColor = color
        if (!hasSetPressedStrokeColor)
            mPressedStrokeColor = color
        if (!hasSetSelectedStrokeColor)
            mSelectedStrokeColor = color
        if (!hasSetFocusedStrokeColor)
            mFocusedStrokeColor = color
        return this
    }

    /**
     * 设置绘制边框不可点击时颜色
     */
    fun disabledStrokeColor(@ColorRes color: Int): ShapeSelector {
        mDisabledStrokeColor = XSelectorHelper.getColorRes(color)
        hasSetDisabledStrokeColor = true
        isLine = true
        return this
    }

    fun disabledStrokeColor(color: String): ShapeSelector {
        mDisabledStrokeColor = Color.parseColor(color)
        hasSetDisabledStrokeColor = true
        isLine = true
        return this
    }

    /**
     * 设置绘制边框触摸时颜色
     */
    fun pressedStrokeColor(@ColorRes color: Int): ShapeSelector {
        mPressedStrokeColor = XSelectorHelper.getColorRes(color)
        hasSetPressedStrokeColor = true
        isLine = true
        return this
    }

    fun pressedStrokeColor(color: String): ShapeSelector {
        mPressedStrokeColor = Color.parseColor(color)
        hasSetPressedStrokeColor = true
        isLine = true
        return this
    }

    /**
     * 设置绘制边框选中时颜色
     */
    fun selectedStrokeColor(@ColorRes color: Int): ShapeSelector {
        mSelectedStrokeColor = XSelectorHelper.getColorRes(color)
        hasSetSelectedStrokeColor = true
        isLine = true
        return this
    }

    fun selectedStrokeColor(color: String): ShapeSelector {
        mSelectedStrokeColor = Color.parseColor(color)
        hasSetSelectedStrokeColor = true
        isLine = true
        return this
    }

    /**
     * 设置绘制边框获取焦点时颜色
     */
    fun focusedStrokeColor(@ColorRes color: Int): ShapeSelector {
        mFocusedStrokeColor = XSelectorHelper.getColorRes(color)
        hasSetFocusedStrokeColor = true
        isLine = true
        return this
    }

    fun focusedStrokeColor(color: String): ShapeSelector {
        mFocusedStrokeColor = Color.parseColor(color)
        hasSetFocusedStrokeColor = true
        isLine = true
        return this
    }

    /**
     * 边框虚线样式
     *
     * @param dashLineColorResId 边框线颜色 例：R.color.colorPrimary
     * @param dashLineWidth      边框虚线宽度 px
     * @param dashWidth          虚线宽度 px
     * @param dashGap            间隙宽度 px
     */
    fun dashLine(
        dashLineWidth: Int, @ColorRes dashLineColorResId: Int,
        dashWidth: Float,
        dashGap: Float
    ): ShapeSelector {
        this.isDashLine = true
        this.dashLineWidth = dashLineWidth
        this.dashWidth = dashWidth
        this.dashGap = dashGap
        this.dashLineColor = XSelectorHelper.getColorRes(dashLineColorResId)
        return this
    }

    fun dashLine(
        dashLineWidth: Int,
        dashLineColor: String,
        dashWidth: Float,
        dashGap: Float
    ): ShapeSelector {
        this.isDashLine = true
        this.dashLineWidth = dashLineWidth
        this.dashWidth = dashWidth
        this.dashGap = dashGap
        this.dashLineColor = Color.parseColor(dashLineColor)
        return this
    }

    /**
     * 渐变样式
     * 默认 线性渐变 GradientDrawable.LINEAR_GRADIENT
     * 默认方向 从上到下渐变 GradientDrawable.Orientation.TOP_BOTTOM
     *
     * @param startColor 渐变开始端颜色 例：R.color.colorPrimary
     * @param endColor   渐变结束端颜色 例：R.color.colorPrimary
     */
    fun gradient(@ColorRes startColor: Int, @ColorRes endColor: Int): ShapeSelector {
        this.isGradient = true
        this.gradientColors = IntArray(2)
        this.gradientColors!![0] = XSelectorHelper.getColorRes(startColor)
        this.gradientColors!![1] = XSelectorHelper.getColorRes(endColor)
        this.gradientType = GradientDrawable.LINEAR_GRADIENT
        this.gradientOrientation = TOP_BOTTOM
        return this
    }

    fun gradient(startColor: String, endColor: String): ShapeSelector {
        this.isGradient = true
        this.gradientColors = IntArray(2)
        this.gradientColors!![0] = Color.parseColor(startColor)
        this.gradientColors!![1] = Color.parseColor(endColor)
        this.gradientType = GradientDrawable.LINEAR_GRADIENT
        this.gradientOrientation = TOP_BOTTOM
        return this
    }

    fun gradientLinear(@ColorRes vararg gradientColorsResId: Int): ShapeSelector {
        this.isGradient = true
        this.gradientType = GradientDrawable.LINEAR_GRADIENT
        this.gradientOrientation = TOP_BOTTOM
        if (gradientColorsResId.size > 1) {
            this.gradientColors = IntArray(gradientColorsResId.size)
            for (i in gradientColorsResId.indices) {
                this.gradientColors!![i] = XSelectorHelper.getColorRes(gradientColorsResId[i])
            }
        } else {
            throw ExceptionInInitializerError("渐变颜色数组至少需要两个颜色")
        }
        return this
    }

    fun gradientLinear(vararg gradientColorsResId: String): ShapeSelector {
        this.isGradient = true
        this.gradientType = GradientDrawable.LINEAR_GRADIENT
        this.gradientOrientation = TOP_BOTTOM
        if (gradientColorsResId.size > 1) {
            this.gradientColors = IntArray(gradientColorsResId.size)
            for (i in gradientColorsResId.indices) {
                this.gradientColors!![i] = Color.parseColor(gradientColorsResId[i])
            }
        } else {
            throw ExceptionInInitializerError("渐变颜色数组至少需要两个颜色")
        }
        return this
    }

    /**
     * 线性渐变样式
     *
     * @param gradientColorsResId 渐变颜色数组 数组元素 例：R.color.colorPrimary
     * @param gradientOrientation 渐变方向
     */
    fun gradientLinear(@GradientOrientation gradientOrientation: String, @ColorRes vararg gradientColorsResId: Int): ShapeSelector {
        this.isGradient = true
        this.gradientType = GradientDrawable.LINEAR_GRADIENT
        this.gradientOrientation = gradientOrientation
        if (gradientColorsResId.size > 1) {
            this.gradientColors = IntArray(gradientColorsResId.size)
            for (i in gradientColorsResId.indices) {
                this.gradientColors!![i] = XSelectorHelper.getColorRes(gradientColorsResId[i])
            }
        } else {
            throw ExceptionInInitializerError("渐变颜色数组至少需要两个颜色")
        }
        return this
    }

    fun gradientLinear(@GradientOrientation gradientOrientation: String, vararg gradientColors: String): ShapeSelector {
        this.isGradient = true
        this.gradientType = GradientDrawable.LINEAR_GRADIENT
        this.gradientOrientation = gradientOrientation
        if (gradientColors.size > 1) {
            this.gradientColors = IntArray(gradientColors.size)
            for (i in gradientColors.indices) {
                this.gradientColors!![i] = Color.parseColor(gradientColors[i])
            }
        } else {
            throw ExceptionInInitializerError("渐变颜色数组至少需要两个颜色")
        }
        return this
    }

    /**
     * 扫描渐变样式
     *
     * @param gradientColors 渐变颜色数组 数组元素 例：R.color.colorPrimary
     */
    fun gradientSweep(vararg gradientColors: Int): ShapeSelector {
        this.isGradient = true
        this.gradientType = GradientDrawable.SWEEP_GRADIENT
        if (gradientColors.size > 1) {
            this.gradientColors = IntArray(gradientColors.size)
            for (i in gradientColors.indices) {
                this.gradientColors!![i] = XSelectorHelper.getColorRes(gradientColors[i])
            }
        } else {
            throw ExceptionInInitializerError("渐变颜色数组至少需要两个颜色")
        }
        return this
    }

    fun gradientSweep(vararg gradientColors: String): ShapeSelector {
        this.isGradient = true
        this.gradientType = GradientDrawable.SWEEP_GRADIENT
        if (gradientColors.size > 1) {
            this.gradientColors = IntArray(gradientColors.size)
            for (i in gradientColors.indices) {
                this.gradientColors!![i] = Color.parseColor(gradientColors[i])
            }
        } else {
            throw ExceptionInInitializerError("渐变颜色数组至少需要两个颜色")
        }
        return this
    }

    /**
     * 辐射渐变样式
     *
     * @param gradientColors 渐变颜色数组 数组元素 例：R.color.colorPrimary
     */
    fun gradientRadial(radialRadius: Float, vararg gradientColors: Int): ShapeSelector {
        this.isGradient = true
        this.gradientType = GradientDrawable.RADIAL_GRADIENT
        this.radialRadius = radialRadius
        if (gradientColors.size > 1) {
            this.gradientColors = IntArray(gradientColors.size)
            for (i in gradientColors.indices) {
                this.gradientColors!![i] = XSelectorHelper.getColorRes(gradientColors[i])
            }
        } else {
            throw ExceptionInInitializerError("渐变颜色数组至少需要两个颜色")
        }
        return this
    }

    fun gradientRadial(radialRadius: Float, vararg gradientColors: String): ShapeSelector {
        this.isGradient = true
        this.gradientType = GradientDrawable.RADIAL_GRADIENT
        this.radialRadius = radialRadius
        if (gradientColors.size > 1) {
            this.gradientColors = IntArray(gradientColors.size)
            for (i in gradientColors.indices) {
                this.gradientColors!![i] = Color.parseColor(gradientColors[i])
            }
        } else {
            throw ExceptionInInitializerError("渐变颜色数组至少需要两个颜色")
        }
        return this
    }

    /**
     * 设置圆角弧度 默认设置4个圆角
     */
    fun radius(radius: Float): ShapeSelector {
        this.isRadius = true
        this.topRightRadius = radius
        this.topLeftRadius = radius
        this.bottomRightRadius = radius
        this.bottomLeftRadius = radius
        return this
    }

    /**
     * 设置右上圆角
     *
     * @param topRightRadius 圆角弧度
     */
    fun trRadius(topRightRadius: Float): ShapeSelector {
        this.isRadius = true
        this.topRightRadius = topRightRadius
        return this
    }

    /**
     * 设置左上圆角
     *
     * @param topLeftRadius 圆角弧度
     */
    fun tlRadius(topLeftRadius: Float): ShapeSelector {
        this.isRadius = true
        this.topLeftRadius = topLeftRadius
        return this
    }

    /**
     * 设置右下圆角
     *
     * @param bottomRightRadius 圆角弧度
     */
    fun brRadius(bottomRightRadius: Float): ShapeSelector {
        this.isRadius = true
        this.bottomRightRadius = bottomRightRadius
        return this
    }

    /**
     * 设置左下圆角
     *
     * @param bottomLeftRadius 圆角弧度
     */
    fun blRadius(bottomLeftRadius: Float): ShapeSelector {
        this.isRadius = true
        this.bottomLeftRadius = bottomLeftRadius
        return this
    }

    /**
     * 设置背景选择器同时设置字体颜色颜色器
     *
     * @param pressedColorResId 触摸颜色 例：R.color.colorPrimary
     * @param normalColorResId  正常颜色 例：R.color.colorPrimary
     */
    fun selectorColor(@ColorRes pressedColorResId: Int, @ColorRes normalColorResId: Int): ShapeSelector {
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
    fun selectorColor(pressedColor: String, normalColor: String): ShapeSelector {
        mColorStateList = ColorSelector.getInstance()
            .pressedColor(pressedColor)
            .defaultColor(normalColor)
            .build()
        this.isSelectorColor = true
        return this
    }


    override fun into(view: View): XSelector {
        if (isBackgroundColor && view !is Button) { //针对selector作用在非button无效问题
            view.isClickable = true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = createShape()
        } else {
            view.setBackgroundDrawable(createShape())
        }
        if (isSelectorColor) {
            try {
                if (view is TextView) {
                    view.setTextColor(mColorStateList)
                } else if (view is Button) {
                    view.setTextColor(mColorStateList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw ExceptionInInitializerError("设置字体颜色选择器（Selector）请传入TextView（包括Button）！！！")
            }

        }
        return XSelector
    }

    override fun build(): Drawable {
        return createShape()
    }

    /**
     * 生成样式
     *
     * @return Drawable
     */
    private fun createShape(): StateListDrawable {
        val selector = StateListDrawable()
        //enabled = false
        if (hasSetDisabledBgColor || hasSetDisabledStrokeColor) {
            val disabledShape = getItemShape(
                mShape,
                mDisabledBgColor, mDisabledStrokeColor, dashLineColor
            )
            selector.addState(intArrayOf(-android.R.attr.state_enabled), disabledShape)
        }

        //pressed = true
        if (hasSetPressedBgColor || hasSetPressedStrokeColor) {
            val pressedShape = getItemShape(
                mShape,
                mPressedBgColor, mPressedStrokeColor, dashLineColor
            )
            selector.addState(intArrayOf(android.R.attr.state_pressed), pressedShape)
        }

        //selected = true
        if (hasSetSelectedBgColor || hasSetSelectedStrokeColor) {
            val selectedShape = getItemShape(
                mShape,
                mSelectedBgColor, mSelectedStrokeColor, dashLineColor
            )
            selector.addState(intArrayOf(android.R.attr.state_selected), selectedShape)
        }

        //focused = true
        if (hasSetFocusedBgColor || hasSetFocusedStrokeColor) {
            val focusedShape = getItemShape(
                mShape,
                mFocusedBgColor, mFocusedStrokeColor, dashLineColor
            )
            selector.addState(intArrayOf(android.R.attr.state_focused), focusedShape)
        }

        //default
        val defaultShape = getItemShape(
            mShape,
            mDefaultBgColor, mDefaultStrokeColor, dashLineColor
        )
        selector.addState(intArrayOf(), defaultShape)

        return selector
    }

    /**
     * 设置线性渐变方向
     */
    private fun createGradientOrientation(): GradientDrawable.Orientation {
        var orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM
        when (gradientOrientation) {
            TOP_BOTTOM -> orientation = GradientDrawable.Orientation.TOP_BOTTOM
            TR_BL -> orientation = GradientDrawable.Orientation.TR_BL
            RIGHT_LEFT -> orientation = GradientDrawable.Orientation.RIGHT_LEFT
            BR_TL -> orientation = GradientDrawable.Orientation.BR_TL
            BOTTOM_TOP -> orientation = GradientDrawable.Orientation.BOTTOM_TOP
            BL_TR -> orientation = GradientDrawable.Orientation.BL_TR
            LEFT_RIGHT -> orientation = GradientDrawable.Orientation.LEFT_RIGHT
            TL_BR -> orientation = GradientDrawable.Orientation.TL_BR
        }
        return orientation
    }

    private fun getItemShape(
        shape: Int,
        solidColor: Int,
        lineColor: Int,
        dashLineColor: Int
    ): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.shape = shape
        if (isRadius) {
            val radii = floatArrayOf(
                dip2px(topLeftRadius).toFloat(),
                dip2px(topLeftRadius).toFloat(),
                dip2px(topRightRadius).toFloat(),
                dip2px(topRightRadius).toFloat(),
                dip2px(bottomRightRadius).toFloat(),
                dip2px(bottomRightRadius).toFloat(),
                dip2px(bottomLeftRadius).toFloat(),
                dip2px(bottomLeftRadius).toFloat()
            )
            drawable.cornerRadii = radii//设置圆角
        }

        if (isBackgroundColor) {
            drawable.setColor(solidColor)// 设置背景颜色
        }

        if (isLine) {
            drawable.setStroke(dip2px(mStrokeWidth.toFloat()), lineColor)// 设置边框线
        }

        if (isDashLine) {
            drawable.setStroke(
                dip2px(dashLineWidth.toFloat()),
                dashLineColor,
                dip2px(dashWidth).toFloat(),
                dip2px(dashGap).toFloat()
            )//设置虚线
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            isGradientCreate(drawable)
        } else {
            throw RuntimeException("渐变只支持JELLY_BEAN以上")
        }

        return drawable
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun isGradientCreate(drawable: GradientDrawable) {
        if (isGradient) {
            when (gradientType) {
                GradientDrawable.LINEAR_GRADIENT -> drawable.orientation =
                    createGradientOrientation()//设置线性渐变方向
                GradientDrawable.RADIAL_GRADIENT -> drawable.gradientRadius =
                    dip2px(radialRadius).toFloat()//设置辐射渐变辐射范围半径
                GradientDrawable.SWEEP_GRADIENT -> {
                }
            }
            drawable.gradientType = gradientType
            drawable.colors = gradientColors
        }
    }
}