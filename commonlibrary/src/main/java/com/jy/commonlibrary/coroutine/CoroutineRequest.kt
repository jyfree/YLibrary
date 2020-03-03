package com.jy.commonlibrary.coroutine

import androidx.lifecycle.LifecycleOwner
import com.jy.baselibrary.utils.YHandlerUtils
import com.jy.baselibrary.utils.YLogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

/**

 * @Author Administrator
 * @Date 2019/10/24-18:19
 * @TODO 协程的性能一般般，不建议用协程
 */
interface CoroutineRequest {
    fun <T> requestCoroutine(
        coroutineResultCallback: CoroutineResultCallback<T>,
        lifecycleOwner: LifecycleOwner,
        block: () -> T?
    ) {
        GlobalScope.asyncWithLifecycle(Dispatchers.Default, lifecycleOwner) {
            try {
                val t = block.invoke()
                YHandlerUtils.runOnUiThread(Runnable {
                    coroutineResultCallback.forResult(t)
                })
            } catch (ex: Exception) {
                YLogUtils.e("CoroutineRequest request  error : ${ex.message}")
                coroutineResultCallback.forResult(null)
            }
        }
    }

    fun <T> requestCoroutine(block: () -> T?) {
        GlobalScope.then(Dispatchers.Default) {
            block.invoke()
        }
    }
}