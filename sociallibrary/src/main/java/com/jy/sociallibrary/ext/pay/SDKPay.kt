package com.jy.sociallibrary.ext.pay

/**
 * @Author Administrator
 * @Date 2019/11/12-14:14
 * @TODO
 */
class SDKPay private constructor() {
    val sdkPayManager: SDKPayManager = SDKPayManager()

    private object Holder {
        val instance = SDKPay()
    }

    companion object {

        val instance: SDKPay
            @Synchronized get() = Holder.instance
    }
}
