package com.jy.sociallibrary.ext.login

/**
 * @Author Administrator
 * @Date 2019/11/12-14:14
 * @TODO
 */
class SDKLogin private constructor() {
    val sdkLoginManager: SDKLoginManager = SDKLoginManager()

    private object Holder {
        val instance = SDKLogin()
    }

    companion object {

        val instance: SDKLogin
            get() = Holder.instance
    }
}
