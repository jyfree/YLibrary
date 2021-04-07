package com.jy.baselibrary

import android.app.Application
import com.jy.baselibrary.sp.SharedPreferencesConfigUtils
import com.jy.baselibrary.thread.LoaderConfiguration
import com.jy.baselibrary.thread.ThreadManage
import com.jy.baselibrary.utils.BaseUtils


/**
 * @Author jy
 * @Date 2019/8/8-16:53
 * @TODO 本库公共配置
 */
object BaseLibraryConfig {

    /**
     * 必须在全局Application onCreate调用
     * @param application Application
     * @param threadLoaderConfig 线程池配置
     */
    fun init(application: Application, threadLoaderConfig: LoaderConfiguration) {
        //初始化基础工具类
        BaseUtils.init(application)
        //初始化全局线程池
        ThreadManage.getInstance().loaderEngine.init(threadLoaderConfig)
        //初始化sp
        SharedPreferencesConfigUtils.getInstance().init()
    }


}