package com.jy.commonlibrary.glide

import android.graphics.*
import com.bumptech.glide.load.Key

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

import java.security.MessageDigest

class GlideCircleWithBorder(private val borderWidth: Float, borderColor: Int) :
    BitmapTransformation() {
    private val ID = javaClass.name
    private var mBorderPaint: Paint? = null


    init {
        mBorderPaint = Paint()
        mBorderPaint?.color = borderColor
        mBorderPaint?.style = Paint.Style.STROKE
        mBorderPaint?.isAntiAlias = true
        mBorderPaint?.strokeWidth = borderWidth
        mBorderPaint?.isDither = true

    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return circleCrop(pool, toTransform)
    }

    private fun circleCrop(bitmapPool: BitmapPool, source: Bitmap): Bitmap {

        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val square = Bitmap.createBitmap(source, x, y, size, size)
        val result = bitmapPool.get(size, size, Bitmap.Config.ARGB_8888)

        //画图
        val canvas = Canvas(result)
        val paint = Paint()
        //设置 Shader
        paint.shader = BitmapShader(square, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val radius = size / 2f
        //绘制一个圆
        canvas.drawCircle(radius, radius, radius, paint)


        //注意：避免出现描边被屏幕边缘裁掉
        val borderRadius = radius - borderWidth / 2
        //画边框
        canvas.drawCircle(radius, radius, borderRadius, mBorderPaint!!)
        return result
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID.toByteArray(Key.CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is GlideCircleWithBorder
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }
}
