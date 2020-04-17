package com.jy.simple.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jy.baselibrary.utils.YLogUtils

/**
 * @description 上传log work
 * @date: 2020/4/17 15:50
 * @author: jy
 */
class UploadLogWork(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {

        //TODO 执行上传log
        YLogUtils.i("hi i am work")

        return Result.success()
    }

}