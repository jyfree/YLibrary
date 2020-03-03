package com.jy.commonlibrary.social.pay

/**
 * @Author Administrator
 * @Date 2019/11/12-14:14
 * @TODO
 */
class ExtPay private constructor() {
    val sdkPayManager: SDKPayManager = SDKPayManager()

    private object Holder {
        val instance = ExtPay()
    }

    companion object {

        val instance: ExtPay
            @Synchronized get() = Holder.instance
    }
}
