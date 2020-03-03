package com.jy.commonlibrary.glide


class ImageLoaderConfiguration {

    var loadImageResId: Int = 0//加载中图片
    var errorImageResId: Int = 0//加载失败图片
    var userIconImageResId: Int = 0//默认头像图片

    fun initImageResId(loadImageResId: Int, errorImageResId: Int, userIconImageResId: Int) {
        this.loadImageResId = loadImageResId
        this.errorImageResId = errorImageResId
        this.userIconImageResId = userIconImageResId
    }

    companion object {
        private val mInstance = ImageLoaderConfiguration()

        fun getInstance(): ImageLoaderConfiguration {
            return mInstance
        }
    }

}
