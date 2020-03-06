package com.jy.sociallibrary.ext.share

/**
 * @Author Administrator
 * @Date 2019/11/12-14:14
 * @TODO
 */
class ExtShare private constructor() {
    val sdkShareManager: SDKShareManager = SDKShareManager()

    private object Holder {
        val instance = ExtShare()
    }

    companion object {

        val instance: ExtShare
            @Synchronized get() = Holder.instance
    }
}
