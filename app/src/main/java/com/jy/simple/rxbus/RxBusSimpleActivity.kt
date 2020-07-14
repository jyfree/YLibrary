package com.jy.simple.rxbus

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseAppCompatActivity
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.baselibrary.utils.ToastUtils
import com.jy.commonlibrary.rxbus.RxBus
import com.jy.commonlibrary.rxbus.Subscribe
import com.jy.commonlibrary.rxbus.ThreadMode
import com.jy.simple.R


/**
 * Administrator
 * created at 2019/9/25 18:54
 * TODO: RxBus 测试
 */
class RxBusSimpleActivity : BaseAppCompatActivity() {

    companion object {

        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, RxBusSimpleActivity::class.java)
        }
    }


    override fun initLayoutID(): Int = R.layout.simple_rxbus_activity

    override fun initUI(savedInstanceState: Bundle?) {
        RxBus.getDefault().register(this)
    }


    fun onSend(view: View) {
        when (view.id) {
            R.id.button -> RxBus.getDefault().send(1001, "hihi")
            R.id.button2 -> RxBus.getDefault().send(1002, 123)
            R.id.button3 -> RxBus.getDefault().send(1003, MsgData(111, "test"))
            R.id.button4 -> RxBus.getDefault().send(1004)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getDefault().unregister(this)
    }

    @SuppressLint("测试1，参数为String类型")
    @Subscribe(code = 1001, threadMode = ThreadMode.MAIN)
    fun rxBusTest1(msg: String) {
        ToastUtils.showToast(this, "RxBus--参数为String类型--接收到信息$msg")
    }

    @SuppressLint("测试2，参数为int基本数据类型")
    @Subscribe(code = 1002, threadMode = ThreadMode.MAIN)
    fun rxBusTest2(type: Int) {
        ToastUtils.showToast(this, "RxBus--参数为int基本数据类型--接收到信息$type")

    }

    @SuppressLint("测试3，参数为自定义对象")
    @Subscribe(code = 1003, threadMode = ThreadMode.MAIN)
    fun rxBusTest3(msgData: MsgData) {
        ToastUtils.showToast(this, "RxBus--参数为自定义对象--接收到信息$msgData")

    }


    @SuppressLint("测试4，无参")
    @Subscribe(code = 1004, threadMode = ThreadMode.MAIN)
    fun rxBusTest4() {
        ToastUtils.showToast(this, "RxBus--无参--接收到信息")

    }


}
