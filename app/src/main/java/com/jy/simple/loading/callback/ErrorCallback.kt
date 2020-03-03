package com.jy.simple.loading.callback

import com.jy.baselibrary.loadsir.callback.Callback
import com.jy.simple.R


/**
 * Administrator
 * created at 2018/12/3 9:12
 * description:LoadSir错误页
 */
class ErrorCallback : Callback() {
    override fun onCreateView(): Int = R.layout.loadsir_callback_error
}