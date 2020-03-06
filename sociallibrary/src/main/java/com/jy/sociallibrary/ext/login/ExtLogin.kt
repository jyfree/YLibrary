package com.jy.sociallibrary.ext.login

/**
 * @Author Administrator
 * @Date 2019/11/12-14:14
 * @TODO
 */
class ExtLogin private constructor() {
    val sdkLoginManager: SDKLoginManager = SDKLoginManager()

    private object Holder {
        val instance = ExtLogin()
    }

    companion object {

        val instance: ExtLogin
            @Synchronized get() = Holder.instance
    }
}
