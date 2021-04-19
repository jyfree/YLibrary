package com.jy.simple.http

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jy.baselibrary.base.BaseActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.commonlibrary.deque.FunTaskManager
import com.jy.commonlibrary.http.RxHelper
import com.jy.commonlibrary.http.RxObserver
import com.jy.simple.R
import com.jy.simple.bean.SendGiftVo
import com.jy.simple.repository.BannerRepository
import com.jy.simple.repository.UserRepository
import com.jy.simple.viewmodel.MvpViewModel
import com.jy.simple.viewmodel.MvpViewModelFactory
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
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

    private lateinit var viewModel: MvpViewModel


    override fun initLayoutID(): Int = R.layout.simple_api_test_activity_queue

    override fun initUI(savedInstanceState: Bundle?) {
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            MvpViewModelFactory(BannerRepository(), UserRepository())
        ).get(MvpViewModel::class.java)
        viewModel.setLifeCycleProvide(this)
        viewModel.setLifecycleOwner(this)
        viewModel.loading.observe(this, Observer<Boolean?> { show -> showPopWindowLoading(show!!) })
    }

    override fun onDestroy() {
        super.onDestroy()
        FunTaskManager.getInstance().getFunQueueTask().releaseAll()
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

        viewModel.userRepository.sendGift(sendGiftVo)
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(lifecycleProvider)
            .subscribe(RxObserver(doNext = {
                tv_msg.setText(it.msg)
            }, doError = { _, _ ->
            }))
    }

    /**
     * 单任务队列
     */
    private fun sendGiftFunSingle(sendGiftVo: SendGiftVo) {

        viewModel.userRepository.sendGift(sendGiftVo)
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(lifecycleProvider)
            .subscribe(RxObserver(doNext = {
                tv_msg.setText(it.msg)
                FunTaskManager.getInstance().getFunQueueTask().consumeSingle()
            }, doError = { _, _ ->
                FunTaskManager.getInstance().getFunQueueTask().consumeSingle()
            }))
    }

    /**
     * 链式队列
     */
    private fun sendGiftFunChain(sendGiftVo: SendGiftVo) {

        viewModel.userRepository.sendGift(sendGiftVo)
            .compose(RxHelper.handleSingleResult())
            .bindToLifecycle(lifecycleProvider)
            .subscribe(RxObserver(doNext = {
                tv_msg.setText(it.msg)
                FunTaskManager.getInstance().getFunQueueTask().consumeChain()
            }, doError = { _, _ ->
                FunTaskManager.getInstance().getFunQueueTask().consumeChain()
            }))
    }
}
