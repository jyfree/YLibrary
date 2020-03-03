package com.jy.baselibrary.utils

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View

/**

 * @Author Administrator
 * @Date 2019/9/25-22:20
 * @TODO
 */
object ViewUtils {
    fun isTouchInsideView(event: MotionEvent, view: View): Boolean {
        val eventX = event.rawX
        val eventY = event.rawY

        val contentArray = IntArray(2)
        view.getLocationOnScreen(contentArray)

        val contentRect = Rect()
        view.getDrawingRect(contentRect)

        contentRect.offsetTo(contentArray[0], contentArray[1])

        return contentRect.contains(eventX.toInt(), eventY.toInt())
    }
}