package com.jy.commonlibrary.coroutine

import androidx.lifecycle.LifecycleOwner
import com.jy.baselibrary.utils.YLogUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


/**
 * @param lifecycleOwner Android生命周期的接口
 *
 * @param context 默认 launch 所创建的 CorouTine 会自动继承当前 Coroutine 的 context，
 * 如果有额外的 context 需要传递给所创建的 Coroutine 则可以通过第一个参数来设置。
 *
 * @param start CoroutineStart 枚举类型，用来指定 Coroutine 启动的选项
 * – DEFAULT （默认值）立刻安排执行该Coroutine实例
 * – LAZY 延迟执行，只有当用到的时候才执行
 * – ATOMIC 类似 DEFAULT，区别是当Coroutine还没有开始执行的时候无法取消
 * – UNDISPATCHED 如果使用 Dispatchers.Unconfined dispatcher，则立刻在当前线程执行直到遇到第一个suspension point 。
 *    然后当 Coroutine 恢复的时候，在继续在 suspension的 context 中设置的 CoroutineDispatcher 中执行。
 *
 * @param block Coroutine 代码块
 */
fun <T> GlobalScope.asyncWithLifecycle(
    context: CoroutineContext,
    lifecycleOwner: LifecycleOwner,
    block: () -> T
) {

    val lift = LifecycleCoroutineListener()
    val job = GlobalScope.launch(context) {
        if (!lift.isDestroy && isActive) {
            YLogUtils.iTag("GlobalScope", "launch", Thread.currentThread().name)
            block()
        }
    }
    lift.setJob(job)
    lifecycleOwner.lifecycle.addObserver(lift)
}

fun <T> GlobalScope.then(context: CoroutineContext, block: () -> T) {
    GlobalScope.launch(context) {
        YLogUtils.iTag("GlobalScope", "then", Thread.currentThread().name)
        block()
    }
}