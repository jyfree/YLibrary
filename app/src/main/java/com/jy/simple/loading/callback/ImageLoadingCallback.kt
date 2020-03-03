package com.jy.simple.loading.callback

import com.jy.baselibrary.loadsir.callback.Callback
import com.jy.simple.R


/**

 * @Author Administrator
 * @Date 2019/1/10-14:46
 * @TODO LoadSir 带默认图片加载页
 */
class ImageLoadingCallback : Callback() {
    override fun onCreateView(): Int = R.layout.loadsir_callback_image_loading
}