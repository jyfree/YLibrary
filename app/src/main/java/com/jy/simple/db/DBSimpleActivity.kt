package com.jy.simple.db

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseAppCompatActivity
import com.jy.baselibrary.thread.lifecycle.ThreadRequest
import com.jy.baselibrary.thread.lifecycle.ThreadResultCallback
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.coroutine.CoroutineRequest
import com.jy.simple.R
import java.util.*


/**

 * @Author Administrator
 * @Date 2019/10/24-18:30
 * @TODO 1、实现CoroutineRequest可以使用协程访问数据库  2、实现ThreadRequest可以使用子线程访问数据库
 */
class DBSimpleActivity : BaseAppCompatActivity(), CoroutineRequest, ThreadRequest {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, DBSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_db_activity

    override fun initUI(savedInstanceState: Bundle?) {
    }

    fun onLoadDB(view: View) {
        when (view.id) {
            //协程
            R.id.load_coroutine -> requestCoroutine()
            R.id.install_coroutine -> installCoroutine()

            //主线程
            R.id.load_def -> requestDef()
            R.id.install_def -> installDef()

            //子线程
            R.id.load_thread -> requestThread()
            R.id.install_thread -> installThread()
        }
    }

    //*****************************协程*****************************************************
    private fun requestCoroutine() {
        val startTime = System.currentTimeMillis()
        for (i in 0..10000) {
            requestCoroutine {
                query()
            }
        }
        YLogUtils.i("协程--10000次--time", System.currentTimeMillis() - startTime)
    }

    private fun installCoroutine() {
        val startTime = System.currentTimeMillis()
        requestCoroutine {
            install("协程")
        }
        YLogUtils.iFormat("协程--插入--用时%sms", System.currentTimeMillis() - startTime)
    }

    //*****************************主线程*****************************************************

    private fun requestDef() {
        val startTime = System.currentTimeMillis()
        for (i in 0..10000) {
            query()
        }
        YLogUtils.i("主线程--10000次--time", System.currentTimeMillis() - startTime)
    }

    private fun installDef() {
        val startTime = System.currentTimeMillis()
        install("主线程")
        YLogUtils.iFormat("主线程--插入--用时%sms", System.currentTimeMillis() - startTime)
    }


    //*****************************子线程*****************************************************

    private fun requestThread() {
        val startTime = System.currentTimeMillis()
        for (i in 0..10000) {
            requestThread(ThreadResultCallback {
                //                YLogUtils.i("子线程--", i, Thread.currentThread().name)
            }, this) {
                query()
            }
        }
        YLogUtils.i("子线程--10000次--time", System.currentTimeMillis() - startTime)
    }

    private fun installThread() {
        val startTime = System.currentTimeMillis()
        requestThread {
            install("子线程")
        }
        YLogUtils.iFormat("子线程--插入--用时%sms", System.currentTimeMillis() - startTime)
    }


    private fun query(): ArrayList<TestInfo> {
        return AppDatabase.instance.getTestInfoDao().listInfo
    }

    private fun install(msg: String) {

        val testInfo = TestInfo()
        testInfo.savePath = msg
        testInfo.connectionTime = 1
        testInfo.testFilter = "testFilter"
        testInfo.testUpdate = "testUpdate"
        testInfo.testUpdateTwo = "testUpdateTwo"
//        testInfo.url="测试地址"
        AppDatabase.instance.getTestInfoDao().insertOrUpdate(testInfo)
    }
}