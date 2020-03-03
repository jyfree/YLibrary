package com.jy.baselibrary.helper

import android.annotation.TargetApi
import android.app.Activity
import android.app.Fragment
import android.os.Build
import android.os.CountDownTimer
import com.jy.baselibrary.utils.YLogUtils

/**
 * @Author Administrator
 * @Date 2019/10/10-10:52
 * @TODO 倒计时帮助类
 */
class CountDownTimerHelper {

    private val TAG = CountDownTimerHelper::class.java.simpleName
    private var timer: CountDownTimer? = null
    private var subscriber: Any? = null
    private var onTickListener: ((Long) -> Unit)? = null
    private var onFinishListener: (() -> Unit)? = null
    private var isShowTickLog = false

    private val isRunning: Boolean
        get() {
            var isRunning = true

            when (subscriber) {
                null -> {
                    isRunning = false
                    cancelTimer()
                }
                is Activity -> {
                    if ((subscriber as Activity).isFinishing) {
                        isRunning = false
                        cancelTimer()
                    }
                }
                is Fragment -> {
                    val tmpFragment = subscriber as Fragment
                    if (tmpFragment.activity == null || tmpFragment.activity.isFinishing) {
                        isRunning = false
                        cancelTimer()
                        return isRunning
                    }
                    if (tmpFragment.isDetached) {
                        isRunning = false
                        cancelTimer()
                        return isRunning
                    }
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    if (tmpFragment.fragmentManager?.isDestroyed == true) {
                        isRunning = false
                        cancelTimer()
                    }
                }
            }
            return isRunning
        }

    private fun interval(millisInFuture: Long, countDownInterval: Long) {
        if (!isRunning) {
            return
        }
        timer = object : CountDownTimer(millisInFuture, countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                if (!isRunning) {
                    return
                }
                if (isShowTickLog) {
                    YLogUtils.iFormatTag(
                        TAG, "定时器--正在执行--【订阅者对象：%s----class对象：%s----timer对象：%s----时间：%s】",
                        subscriber, this@CountDownTimerHelper, this, millisUntilFinished
                    )

                }
                onTickListener?.invoke(millisUntilFinished)
            }

            override fun onFinish() {
                cancelTimer()
                if (isShowTickLog) {
                    YLogUtils.iFormatTag(
                        TAG, "定时器--执行完成--【订阅者对象：%s----class对象：%s----timer对象：%s】",
                        subscriber, this@CountDownTimerHelper, this
                    )
                }
                onFinishListener?.invoke()
            }
        }
        timer?.start()

    }


    /**
     * 停止计时器
     */
    fun cancelTimer() {
        if (isShowTickLog) {
            YLogUtils.iFormatTag(
                TAG, "定时器--停止执行--【订阅者对象：%s----class对象：%s----timer对象：%s】",
                subscriber, this@CountDownTimerHelper, timer
            )
        }
        timer?.cancel()
        timer = null
    }

    private fun setBuilder(builder: Builder) {

        subscriber = builder.subscriber
        onTickListener = builder.onTickListener
        onFinishListener = builder.onFinishListener
        isShowTickLog = builder.isShowTickLog
        interval(builder.millisInFuture, builder.countDownInterval)

    }

    class Builder {

        var subscriber: Any? = null
        var millisInFuture: Long = 0
        var countDownInterval: Long = 0
        var onTickListener: ((Long) -> Unit)? = null
        var onFinishListener: (() -> Unit)? = null
        var isShowTickLog = false

        fun register(subscriber: Any): Builder {
            this.subscriber = subscriber
            return this
        }

        fun showTickLog(isShow: Boolean): Builder {
            isShowTickLog = isShow
            return this
        }

        fun interval(millisInFuture: Long, countDownInterval: Long): Builder {
            this.millisInFuture = millisInFuture
            this.countDownInterval = countDownInterval
            return this
        }

        fun onTickCallback(onTickListener: ((Long) -> Unit)?): Builder {
            this.onTickListener = onTickListener
            return this
        }

        fun onFinishCallback(onFinishListener: (() -> Unit)?): Builder {
            this.onFinishListener = onFinishListener
            return this
        }

        fun build(): CountDownTimerHelper {
            val timerHelper = CountDownTimerHelper()
            timerHelper.setBuilder(this)
            return timerHelper
        }
    }

    companion object {

        fun beginBuilder(): Builder {
            return Builder()
        }
    }
}
