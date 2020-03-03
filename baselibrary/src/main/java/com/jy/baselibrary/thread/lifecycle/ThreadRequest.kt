package com.jy.baselibrary.thread.lifecycle

import androidx.lifecycle.LifecycleOwner
import com.jy.baselibrary.utils.YHandlerUtils
import com.jy.baselibrary.utils.YLogUtils


/**

 * @Author Administrator
 * @Date 2019/10/24-18:19
 * @TODO
 */
interface ThreadRequest {
    fun <T> requestThread(
        threadResultCallback: ThreadResultCallback<T>,
        lifecycleOwner: LifecycleOwner,
        block: () -> T?
    ) {

        executeThreadWithLifecycle(lifecycleOwner) {
            try {
                val t = block.invoke()
                YHandlerUtils.runOnUiThread(Runnable {
                    threadResultCallback.forResult(t)
                })
            } catch (ex: Exception) {
                YLogUtils.e("ThreadRequest request  error : ${ex.message}")
                threadResultCallback.forResult(null)
            }
        }
    }

    fun <T> requestThread(block: () -> T?) {
        executeThread {
            block.invoke()
        }
    }

}