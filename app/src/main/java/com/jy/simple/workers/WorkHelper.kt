package com.jy.simple.workers

import androidx.work.*
import com.jy.baselibrary.utils.BaseUtils
import java.util.concurrent.Executors

/**
 * @description Work 助手
 * @date: 2020/4/17 15:51
 * @author: jy
 */
object WorkHelper {

    fun initWorkManager() {
        WorkManager.initialize(BaseUtils.getApp(), Configuration.Builder()
                .setExecutor(Executors.newFixedThreadPool(1))
                .build()
        )
    }

    fun submitLog() {
        // 约束
        val constraints: Constraints = Constraints.Builder()
            .setRequiresCharging(true)//充电
            .setRequiredNetworkType(NetworkType.CONNECTED)//有网络
            .build()
        //WorkRequest
        val logWork = OneTimeWorkRequestBuilder<UploadLogWork>().setConstraints(constraints).build()

        WorkManager.getInstance(BaseUtils.getApp()).enqueue(logWork)
    }
}