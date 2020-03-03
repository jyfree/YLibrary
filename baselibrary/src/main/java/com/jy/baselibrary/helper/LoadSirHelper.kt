package com.jy.baselibrary.helper

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager.BadTokenException
import com.jy.baselibrary.interceptor.PopupWindowTouchInterceptor
import com.jy.baselibrary.loadsir.callback.SuccessCallback
import com.jy.baselibrary.loadsir.core.LoadService
import com.jy.baselibrary.loadsir.core.LoadSir
import com.jy.baselibrary.utils.ScreenResolutionUtils
import com.jy.baselibrary.utils.YHandlerUtils.runOnUiThread
import com.jy.baselibrary.utils.YLogUtils.e
import com.jy.baselibrary.widget.CustomPopWindow
import com.jy.baselibrary.widget.CustomPopWindow.PopupWindowBuilder

/**
 * @Author Administrator
 * @Date 2019/9/25-21:59
 * @TODO 加载器帮助类
 */
class LoadSirHelper {
    //customPopupWindow
    private var customPopWindow: CustomPopWindow? = null
    //loading view
    private val loadSir = lazy { LoadSir.getDefault() }.value
    private var loadService: LoadService<*>? = null
    private fun initPopupWindowLoading(context: Context) {
        require(loadSir.resLayoutIdOfPopWindow != LoadSir.EMPTY_LAYOUT) { "LayoutId no found..." }
        if (customPopWindow == null) {
            val screenResolution =
                ScreenResolutionUtils.getResolution(context)
            customPopWindow =
                PopupWindowBuilder(context).setView(loadSir.resLayoutIdOfPopWindow)
                    .size(screenResolution.first, screenResolution.second)
                    .enableBackgroundDark(true)
                    .setTouchIntercepter(PopupWindowTouchInterceptor())
                    .create()
        }
    }

    fun showPopWindowLoading(context: Activity, flag: Boolean) {
        showPopWindowLoading(context, context.findViewById(android.R.id.content), flag)
    }

    /**
     * 注意：popWindow必须依附于某一个view，而在onCreate中view还没有加载完毕，必须要等activity的生命周期函数全部执行完毕
     *
     * @param context 上下文
     * @param parent  所依附的view
     * @param flag    是否显示
     */
    fun showPopWindowLoading(context: Context?, parent: View?, flag: Boolean) {
        try {
            if (context == null) return
            initPopupWindowLoading(context)
            if (customPopWindow == null) return
            if (flag) {
                customPopWindow!!.showAtLocation(parent, Gravity.CENTER, 0, 0)
            } else {
                disMissPopWindow()
            }
        } catch (e: BadTokenException) {
            e.printStackTrace()
            e(e.message)
        }
    }

    private fun disMissPopWindow() {
        runOnUiThread(Runnable {
            try {
                customPopWindow?.dissmiss()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                e(e.message)
            }
        })
    }

    fun showLoading(target: Any?, flag: Boolean) {
        requireNotNull(loadSir.loadingCallbackClass) { "LoadingCallbackClass no found..." }
        if (loadService == null) {
            loadService = loadSir.register(target!!)
        }
        loadService?.showCallback(if (flag) loadSir.loadingCallbackClass else SuccessCallback::class.java)
    }
}