package com.jy.simple.timer

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jy.baselibrary.base.BaseActivity
import com.jy.baselibrary.helper.CountDownTimerHelper
import com.jy.baselibrary.utils.ActivityUtils
import com.jy.simple.R
import kotlinx.android.synthetic.main.simple_timer_activity.*

/**

 * @Author Administrator
 * @Date 2019/10/10-11:22
 * @TODO 倒计时示例
 */
class TimerSimpleActivity : BaseActivity() {

    companion object {
        fun startAct(context: Context) {
            ActivityUtils.startActivity(context, TimerSimpleActivity::class.java)
        }
    }

    override fun initLayoutID(): Int = R.layout.simple_timer_activity


    override fun initUI(savedInstanceState: Bundle?) {
        btn_start1.isEnabled = true
        btn_start2.isEnabled = true
    }

    fun onClickTimer(view: View) {

        when (view.id) {
            R.id.btn_start1 -> startTimer1()
            R.id.btn_start2 -> startTimer2()
        }
    }

    private fun startTimer1() {
        btn_start1.isEnabled = false
        CountDownTimerHelper.beginBuilder()
                .register(this)
                .showTickLog(true)
                .interval(10000, 100)
                .onTickCallback {
                    tv_timer1.text = (it / 100).toString()
                }
                .onFinishCallback {
                    tv_timer1.text = "倒计时结束"
                    btn_start1.isEnabled = true
                }
                .build()
    }

    private fun startTimer2() {
        btn_start2.isEnabled = false
        CountDownTimerHelper.beginBuilder()
                .register(this)
                .showTickLog(true)
                .interval(20000, 1000)
                .onTickCallback {
                    tv_timer2.text = (it / 1000).toString()
                }
                .onFinishCallback {
                    tv_timer2.text = "倒计时结束"
                    btn_start2.isEnabled = true
                }
                .build()
    }
}