package com.jy.baselibrary.thread.lifecycle


import androidx.lifecycle.LifecycleOwner
import com.jy.baselibrary.thread.ThreadManage
import com.jy.baselibrary.utils.YLogUtils

/**

 * @Author Administrator
 * @Date 2019/10/25-16:00
 * @TODO
 */

fun <T> executeThreadWithLifecycle(lifecycleOwner: LifecycleOwner, block: () -> T) {

    var life: LifecycleThreadListener? = null
    val thread = object : Thread() {

        override fun run() {
            try {
                if (life?.isDestroy == false) {
                    block()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
                YLogUtils.e("executeThread--InterruptedException", e.message)
            }
        }
    }
    life = LifecycleThreadListener(thread)
    lifecycleOwner.lifecycle.addObserver(life)

    submit(thread)


}

fun <T> executeThread(block: () -> T) {

    val thread = object : Thread() {

        override fun run() {
            block()
        }
    }
    submit(thread)
}


fun submit(task: Thread) {
    ThreadManage.getInstance().loaderEngine.submit(task)
}