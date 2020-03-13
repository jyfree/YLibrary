package com.jy.baselibrary.deque.task

import com.jy.baselibrary.utils.YLogUtils
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

/**
 * 方法体任务队列
 * 注：
 * offer() ：如果BlockingQueue可以容纳,则返回true,否则返回false.（本方法不阻塞当前执行方法的线程）
 * poll() ：若队列为空，返回null（本方法不阻塞当前执行方法的线程）
 * peek() ：取出队头的元素，并不删除
 *
 * 1、produceChain与consumeChain需要成对出现
 * 2、produceSingle与consumeSingle需要成对出现
 *
 */
class FunQueueTask<T> {

    //链式任务队列
    private var chainBasket: BlockingQueue<() -> T?> = LinkedBlockingQueue()
    //链式任务队列标记位
    private var flag = true

    //单任务队列
    private var singleBasket: BlockingQueue<() -> T?> = LinkedBlockingQueue(1)

    //******************************链式队列*************************************************
    fun produceChain(block: () -> T?): Boolean {
        val isSucceed = chainBasket.offer(block)
        YLogUtils.d("链式任务--生产", block, "成功?", isSucceed)
        //如果队列只有一个元素，则立马出列
        if (flag && isSucceed) {
            flag = false
            val t = chainBasket.poll()
            YLogUtils.d("链式任务--队列只有一个元素，立马消费", t)
            t?.invoke()
        }
        return isSucceed
    }

    fun consumeChain() {
        val t = chainBasket.poll()
        YLogUtils.d("链式任务--消费", t)
        t?.invoke()
        //若方法体队列已全部出列，则标记为true
        if (t == null) {
            flag = true
        }
    }

    //*******************************单任务队列*************************************************
    fun produceSingle(block: () -> T?): Boolean {
        val isSucceed = singleBasket.offer(block)
        YLogUtils.d("单任务--生产", block, "成功?", isSucceed)
        //如果队列有元素，则立马取出，但不消费
        if (isSucceed) {
            val t = singleBasket.peek()
            YLogUtils.d("单任务--队列有元素，取出元素，但不消费", t)
            t?.invoke()
        }
        return isSucceed
    }

    fun consumeSingle() {
        val t = singleBasket.poll()
        YLogUtils.d("单任务-消费", t)
    }
}