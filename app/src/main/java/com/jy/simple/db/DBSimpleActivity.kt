package com.jy.simple.db

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseAppCompatActivity
import com.jy.baselibrary.thread.lifecycle.ThreadResultCallback
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.YLogUtils
import com.jy.commonlibrary.coroutine.CoroutineResultCallback
import com.jy.commonlibrary.db.DownloadDao
import com.jy.simple.R
import com.jy.simple.db.bean.TestInfo


/**

 * @Author Administrator
 * @Date 2019/10/24-18:30
 * @TODO
 */
class DBSimpleActivity : BaseAppCompatActivity() {

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
            //协程方式查询db
            R.id.load_coroutine -> requestCoroutine()
            //主线程查询db
            R.id.load_def -> requestDef()
            //子线程查询db
            R.id.load_thread -> requestThread()
            //反射 插入&查询db
            R.id.reflect -> insertAndQueryReflectDB()
        }
    }

    private fun requestCoroutine() {
        val startTime = System.currentTimeMillis()
        for (i in 0..10000) {
            DownloadDao.getListInfoToCoroutine(CoroutineResultCallback {
                YLogUtils.i("协程--data", it, i, Thread.currentThread().name)
            }, this)
        }
        YLogUtils.i("协程--全部--time", System.currentTimeMillis() - startTime)
    }

    private fun requestDef() {
        val startTime = System.currentTimeMillis()
        for (i in 0..10000) {
            val itemStartTime = System.currentTimeMillis()
            DownloadDao.getListInfo()
            YLogUtils.i("主线程--单次--time", System.currentTimeMillis() - itemStartTime, i)
        }
        YLogUtils.i("主线程--全部--time", System.currentTimeMillis() - startTime)
    }

    private fun requestThread() {
        val startTime = System.currentTimeMillis()
        for (i in 0..10000) {
            DownloadDao.getListInfoToThread(ThreadResultCallback {
                YLogUtils.i("子线程--data", it, i, Thread.currentThread().name)
            }, this)
        }
        YLogUtils.i("子线程--全部--time", System.currentTimeMillis() - startTime)
    }

    var connectionTime = 0

    private fun insertAndQueryReflectDB() {

        connectionTime++


        val testInfo = TestInfo()
        testInfo.savePath = "http//:www.baidu.com"
        testInfo.connectionTime = connectionTime
        testInfo.testFilter = "testFilter"
        testInfo.testUpdate = "testUpdate"
        testInfo.testUpdateTwo = "testUpdateTwo"
//        testInfo.url="测试地址"
        TestDao.getInstance().insertOrUpdate(testInfo)

        val startTime = System.currentTimeMillis()
        val list = TestDao.getInstance().getListInfo()
        list.forEach {
            YLogUtils.iFormat("单条数据--id：%s--savePath：%s--connectionTime：%s--testFilter：%s--testUpdate：%s--testUpdateTwo：%s",
                    it.id, it.savePath, it.connectionTime, it.testFilter, it.testUpdate, it.testUpdateTwo)
        }
        YLogUtils.iFormat("查询--用时%sms--数据:%s", System.currentTimeMillis() - startTime, list)

    }
}