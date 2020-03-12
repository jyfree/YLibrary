package com.jy.simple.http

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseActivity
import com.jy.baselibrary.deque.task.FunTaskManager
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.commonlibrary.http.RxObserver
import com.jy.simple.R
import com.jy.simple.http.bean.SendGiftVo
import com.jy.simple.http.mvp.ApiSimpleModel
import kotlinx.android.synthetic.main.simple_api_test_activity_queue.*

/**
 * @Author Administrator
 * @Date 2020/3/12-11:38
 * @TODO mvc 任务队列请求
 */
class QueueApiSimpleActivity : BaseActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, QueueApiSimpleActivity::class.java)
        }
    }

    private val simpleMode = ApiSimpleModel()

    override fun initLayoutID(): Int = R.layout.simple_api_test_activity_queue

    override fun initUI(savedInstanceState: Bundle?) {

    }

    fun onRequest(view: View) {
        when (view.id) {
            R.id.requestData_common -> {
                for (i in 1..10) {
                    sendGiftCommon(SendGiftVo("123", "456", 1, 100, 0))
                }
            }
            R.id.requestData_fun_single -> {
                for (i in 1..10) {
                    FunTaskManager.getInstance().getFunQueueTask().produceSingle {
                        sendGiftFunSingle(SendGiftVo("123", "456", 1, 100, 0))
                    }
                }
            }
            R.id.requestData_fun_chain -> {
                for (i in 1..10) {
                    FunTaskManager.getInstance().getFunQueueTask().produceChain {
                        sendGiftFunChain(SendGiftVo("123", "456", 1, 100, 0))
                    }
                }
            }
        }
    }


    /**
     * 普通请求
     */
    private fun sendGiftCommon(sendGiftVo: SendGiftVo) {
        simpleMode.sendGift(RxObserver(doNext = { it ->
            tv_msg.setText(it.msg)
        }, doError = { _, _ ->
        }), lifecycleProvider, sendGiftVo)
    }

    /**
     * 单任务队列
     */
    private fun sendGiftFunSingle(sendGiftVo: SendGiftVo) {
        simpleMode.sendGift(RxObserver(doNext = { it ->
            tv_msg.setText(it.msg)
            FunTaskManager.getInstance().getFunQueueTask().consumeSingle()
        }, doError = { _, _ ->
            FunTaskManager.getInstance().getFunQueueTask().consumeSingle()
        }), lifecycleProvider, sendGiftVo)
    }

    /**
     * 链式队列
     */
    private fun sendGiftFunChain(sendGiftVo: SendGiftVo) {
        simpleMode.sendGift(RxObserver(doNext = { it ->
            tv_msg.setText(it.msg)
            FunTaskManager.getInstance().getFunQueueTask().consumeChain()
        }, doError = { _, _ ->
            FunTaskManager.getInstance().getFunQueueTask().consumeChain()
        }), lifecycleProvider, sendGiftVo)
    }
}
