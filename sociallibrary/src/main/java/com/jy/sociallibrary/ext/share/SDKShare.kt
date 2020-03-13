package com.jy.sociallibrary.ext.share

/**
 * @Author Administrator
 * @Date 2019/11/12-14:14
 * @TODO
 */
class SDKShare private constructor() {
    val sdkShareManager: SDKShareManager = SDKShareManager()

    private object Holder {
        val instance = SDKShare()
    }

    companion object {

        val instance: SDKShare
            @Synchronized get() = Holder.instance
    }
}
