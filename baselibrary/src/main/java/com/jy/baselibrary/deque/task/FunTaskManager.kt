package com.jy.baselibrary.deque.task

/**
 * 任务经营者(单例，全局任务)
 */
class FunTaskManager private constructor() {
    //方法体任务队列
    private val funQueueTask = FunQueueTask<Any>()

    fun getFunQueueTask(): FunQueueTask<Any> {
        return funQueueTask
    }

    private object Holder {
        val instance = FunTaskManager()
    }

    companion object {

        @Synchronized
        fun getInstance(): FunTaskManager {
            return Holder.instance
        }
    }
}