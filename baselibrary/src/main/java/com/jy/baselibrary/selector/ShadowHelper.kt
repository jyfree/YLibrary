package com.jy.baselibrary.selector

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import com.jy.baselibrary.selector.XSelectorHelper.dip2px

class ShadowHelper private constructor() : Drawable() {
    companion object {
        const val SHAPE_ROUND = 1
        const val SHAPE_CIRCLE = 2

        const val ALL = 0x1111
        const val LEFT = 0x0001
        const val TOP = 0x0010
        const val RIGHT = 0x0100
        const val BOTTOM = 0x1000

        fun getInstance() = ShadowHelper()
    }

    private val mPaint: Paint
    /**
     * 阴影模糊半径，越大越模糊
     */
    private var mShadowRadius: Int = 0
    /**
     * 阴影颜色
     */
    private var mShadowColor: Int = 0
    /**
     * 背景形状
     */
    private var mShape: Int = 0
    /**
     * 背景圆角半径
     */
    private var mShapeRadius: Int = 0
    /**
     * 阴影x偏移(右偏移)
     */
    private var mOffsetX: Int = 0
    /**
     * 阴影y偏移(下偏移)
     */
    private var mOffsetY: Int = 0
    /**
     * 背景颜色
     */
    private var mBgColor: IntArray
    private var mRect: RectF? = null

    /**
     * 阴影边 例：0x1100 表示RIGHT和BOTTOM
     */
    private var shadowSide = ALL

    init {
        mShape = SHAPE_ROUND
        mShapeRadius = 0
        mShadowColor = Color.parseColor("#4d000000")
        mShadowRadius = 0
        mOffsetX = 0
        mOffsetY = 0
        mBgColor = IntArray(1)
        mBgColor[0] = Color.TRANSPARENT

        mPaint = Paint()
        mPaint.color = Color.TRANSPARENT
        mPaint.isAntiAlias = true
        mPaint.setShadowLayer(
            mShadowRadius.toFloat(),
            mOffsetX.toFloat(),
            mOffsetY.toFloat(),
            mShadowColor
        )
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
    }

    /**
     * 设置阴影形状 SHAPE_ROUND SHAPE_CIRCLE
     */
    fun setShape(mShape: Int): ShadowHelper {
        this.mShape = mShape
        return this
    }

    /**
     * 设置阴影位置 ALL LEFT TOP RIGHT BOTTOM
     */
    fun setShadowSide(shadowSide: Int): ShadowHelper {
        this.shadowSide = shadowSide
        return this
    }

    /**
     * 设置背景圆角半径
     */
    fun setShapeRadius(ShapeRadius: Float): ShadowHelper {
        this.mShapeRadius = dip2px(ShapeRadius)
        return this
    }

    /**
     * 设置阴影颜色
     * @param shadowColor 例：R.color.colorPrimary
     */
    fun setShadowColor(shadowColor: Int): ShadowHelper {
        this.mShadowColor = XSelectorHelper.getColorRes(shadowColor)
        mPaint.setShadowLayer(
            mShadowRadius.toFloat(),
            mOffsetX.toFloat(),
            mOffsetY.toFloat(),
            mShadowColor
        )
        return this
    }

    fun setShadowColor(shadowColor: String): ShadowHelper {
        this.mShadowColor = Color.parseColor(shadowColor)
        mPaint.setShadowLayer(
            mShadowRadius.toFloat(),
            mOffsetX.toFloat(),
            mOffsetY.toFloat(),
            mShadowColor
        )
        return this
    }

    /**
     * 设置阴影圆角半径
     */
    fun setShadowRadius(shadowRadius: Float): ShadowHelper {
        this.mShadowRadius = dip2px(shadowRadius)
        mPaint.setShadowLayer(
            mShadowRadius.toFloat(),
            mOffsetX.toFloat(),
            mOffsetY.toFloat(),
            mShadowColor
        )
        return this
    }

    fun setOffsetX(OffsetX: Float): ShadowHelper {
        this.mOffsetX = dip2px(OffsetX)
        mPaint.setShadowLayer(
            mShadowRadius.toFloat(),
            mOffsetX.toFloat(),
            mOffsetY.toFloat(),
            mShadowColor
        )
        return this
    }

    fun setOffsetY(OffsetY: Float): ShadowHelper {
        this.mOffsetY = dip2px(OffsetY)
        mPaint.setShadowLayer(
            mShadowRadius.toFloat(),
            mOffsetX.toFloat(),
            mOffsetY.toFloat(),
            mShadowColor
        )
        return this
    }

    /**
     * 设置背景色
     * @param BgColor 例：R.color.colorPrimary
     */
    fun setBgColor(bgColor: Int): ShadowHelper {
        this.mBgColor[0] = XSelectorHelper.getColorRes(bgColor)
        return this
    }

    fun setBgColor(bgColor: String): ShadowHelper {
        this.mBgColor[0] = Color.parseColor(bgColor)
        return this
    }

    fun setBgColor(BgColor: IntArray): ShadowHelper {
        this.mBgColor = BgColor
        return this
    }

    fun setBgColor(bgColor: Array<String>): ShadowHelper {
        val length = bgColor.size
        val color = IntArray(length)
        for (i in 0 until length) {
            color[i] = Color.parseColor(bgColor[i])
        }
        this.mBgColor = color
        return this
    }

    fun setShadowAlpha(i: Int): ShadowHelper {
        alpha = i
        return this
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        val leftShadow =
            if (shadowSide and LEFT == LEFT) mShadowRadius - mOffsetX else -mShapeRadius
        val topShadow = if (shadowSide and TOP == TOP) mShadowRadius - mOffsetY else -mShapeRadius
        val rightShadow =
            if (shadowSide and RIGHT == RIGHT) mShadowRadius + mOffsetX else -mShapeRadius
        val bottomShadow =
            if (shadowSide and BOTTOM == BOTTOM) mShadowRadius + mOffsetY else -mShapeRadius
        if (bounds != null) {
            mRect = RectF(
                (bounds.left + leftShadow).toFloat(),
                (bounds.top + topShadow).toFloat(),
                (bounds.right - rightShadow).toFloat(),
                (bounds.bottom - bottomShadow).toFloat()
            )
        }
    }

    override fun draw(canvas: Canvas) {
        val newPaint = Paint()
        if (mRect == null) return
        if (mBgColor.size == 1) {
            newPaint.color = mBgColor[0]
        } else {
            newPaint.shader =
                LinearGradient(
                    mRect!!.left,
                    mRect!!.height() / 2,
                    mRect!!.right,
                    mRect!!.height() / 2,
                    mBgColor,
                    null,
                    Shader.TileMode.CLAMP
                )
        }
        newPaint.isAntiAlias = true
        if (mShape == SHAPE_ROUND) {
            canvas.drawRoundRect(mRect!!, mShapeRadius.toFloat(), mShapeRadius.toFloat(), mPaint)
            canvas.drawRoundRect(mRect!!, mShapeRadius.toFloat(), mShapeRadius.toFloat(), newPaint)
        } else {
            canvas.drawCircle(
                mRect!!.centerX(),
                mRect!!.centerY(),
                Math.min(mRect!!.width(), mRect!!.height()) / 2,
                mPaint
            )
            canvas.drawCircle(
                mRect!!.centerX(),
                mRect!!.centerY(),
                Math.min(mRect!!.width(), mRect!!.height()) / 2,
                newPaint
            )
        }
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    fun into(view: View?) {
        if (view == null) {
            return
        }
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        ViewCompat.setBackground(view, this)
    }
}